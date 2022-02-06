package com.itcluster.mobile.app.ext.log

import android.content.Context
import com.itcluster.mobile.app.ext.log.Level.Companion.DEBUG
import com.itcluster.mobile.app.ext.log.Level.Companion.ERROR
import com.itcluster.mobile.app.ext.log.Level.Companion.FATAL
import com.itcluster.mobile.app.ext.log.Level.Companion.INFO
import com.itcluster.mobile.app.ext.log.Level.Companion.TRACE
import com.itcluster.mobile.app.ext.log.Level.Companion.WARNING
import okio.sink
import com.itcluster.mobile.app.ext.log.LogEntry.Companion.builder
import okio.source
import okio.buffer
import java.io.*
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Class for managing logs. Should be initialised via [.init] before use. There you can also obtain loggers.
 *
 * @author faerytea
 */
class LogsManager private constructor(name: String, ctx: Context) {
    private var writer: WriterThread? = null

    @Volatile
    private var level = order(Level.TRACE)
    fun logCrashes(dir: String) {
        val d = File(dir)
        val out = ByteArrayOutputStream(100)
        val sink = out.sink()
        val files = d.listFiles() ?: return
        val builder = StringBuilder(files.size.toString() + " crashes found:")
        for (file in files) {
            builder.append('\n')
                .append(file.absolutePath)
        }
        log(
            builder()
                .level(Level.TRACE)
                .name("logCrashes")
                .message(builder.toString())
                .build()
        )
        for (f in files) {
            if (!f.isFile) {
                continue
            }
            try {
                val bufferedSource = f.source().buffer()
                bufferedSource.readAll(sink)
                log(
                    builder()
                        .name("Crash report found")
                        .message(String(out.toByteArray(), StandardCharsets.UTF_8))
                        .level(Level.FATAL)
                        .build()
                )
                bufferedSource.close()
                if (!f.delete()) {
                    log(
                        builder()
                            .message("Cannot delete $f. This report may be duplicated.")
                            .name("logCrashes")
                            .level(Level.WARNING)
                            .build()
                    )
                }
            } catch (e: IOException) {
                log(
                    builder()
                        .level(Level.WARNING)
                        .name("logCrashes")
                        .error(e)
                        .message("Cannot read crash log")
                        .build()
                )
            }
            out.reset()
        }
    }

    /**
     * Set lowest log level. All messages with lower level will be discarded
     *
     * @param logLevel lowest level which will be logged.
     */
    fun setLogLevel( logLevel: String?) {
        if (logLevel == null || logLevel.isEmpty()) {
            log(
                builder()
                    .message("Got empty log level")
                    .name("Log level change")
                    .level(WARNING)
                    .build()
            )
            return
        }
        var parsedLogLevel: String = logLevel
        if (parsedLogLevel.length > 1) {
            parsedLogLevel = parsedLogLevel.substring(0, 1)
        }
        parsedLogLevel = parsedLogLevel.uppercase()
        when (parsedLogLevel) {
            TRACE, DEBUG, INFO, WARNING, ERROR, FATAL -> {
                val newLevel = order(parsedLogLevel)
                synchronized(this) {
                    if (newLevel == level) {
                        return
                    }
                    log(
                        builder()
                            .message("Lowest log level was changed from " + order(level) + " to " + parsedLogLevel)
                            .name("Log level change")
                            .level(Level.INFO)
                            .build()
                    )
                    level = newLevel
                }
            }
            else -> log(
                builder()
                    .message(
                        "Someone set log level to [$logLevel] but that level is not defined. Ignored, log level is " + order(
                            level
                        )
                    )
                    .name("Log level change")
                    .level(Level.WARNING)
                    .build()
            )
        }
    }

    /**
     * Change log file size on fly.
     *
     * @param bytes new size in bytes
     * @see WriterThread.setLogSize
     */
    fun changeLogSize(bytes: Long) {
        writer!!.setLogSize(bytes)
    }

    /**
     * Change log file size on fly.
     *
     * @param kilobytes new size in kilobytes
     * @see WriterThread.setLogSize
     */
    fun changeLogSizeKB(kilobytes: Long) {
        writer!!.setLogSize(kilobytes * 1024)
    }

    /**
     * Creates an archive with properly formatted log files.
     *
     * @param  providers дополнительные провайдеры информации для сохранения в файл архива
     * @return byte array representation of archive.
     * @throws IOException if a read error occurs or if zip stream throw an error
     */
    @Throws(IOException::class)
    fun createArchive(vararg providers: ArchiveLogEntryProvider): ByteArray {
        val bos = ByteArrayOutputStream()
        val zos = ZipOutputStream(bos)
        writeAppLogs(zos)
        try {
            writeAdditionalLogProviders(zos, providers)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        zos.close()
        return bos.toByteArray()
    }

    /**
     * Превращает [LogEntry] в массив байт и передает его [WriterThread] для записи в файл
     *
     * @param entry данные, которые нужно залогировать в файл
     */
    fun log(entry: LogEntry) {
        try {
            if (!shouldBeLogged(order(entry.level))) {
                return
            }
            val builder = ByteArrayBuilder
                .builder()
                .add(java.lang.Long.toString(entry.time)).add(SPACE_AFTER_TIME)
                .add(entry.name).add(SPACE)
                .add(entry.level).add(SPACE)
            if (entry.message != null) {
                builder.add(escapeControls(entry.message.toByteArray(StandardCharsets.UTF_8))).add(
                    SPACE
                )
            }
            if (entry.error != null) {
                val sw = StringWriter(500).append(entry.error.toString()).append('\n')
                entry.error.printStackTrace(PrintWriter(sw))
                builder.add(escapeControls(sw.toString().toByteArray(StandardCharsets.UTF_8)))
            }
            writer!!.log(builder.add('\n').build())
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun writeAppLogs(zos: ZipOutputStream) {
        val entry = ZipEntry("vtb_mb_android.log")
        val original = writer!!.readLogFile()
        val logSize = original.size * 4096
        val log = ByteArray(logSize)
        var shift = 0
        for (part in original) {
            System.arraycopy(part, 0, log, shift, part.size)
            shift += part.size
        }
        entry.size = log.size.toLong()
        zos.putNextEntry(entry)
        zos.write(log)
        zos.closeEntry()
    }

    @Throws(IOException::class)
    private fun writeAdditionalLogProviders(
        zos: ZipOutputStream,
        providers: Array<out ArchiveLogEntryProvider>
    ) {
            for (provider in providers) {
                val entry = ZipEntry(provider.logEntryName)
                val log = provider.logEntryContent
                entry.size = log.size.toLong()
                zos.putNextEntry(entry)
                zos.write(log)
                zos.closeEntry()
            }
    }

    private fun shouldBeLogged(targetLevel: Int): Boolean {
        return targetLevel >= level
    }

    companion object {
        private val REPLACEMENTS = arrayOf(
            "[NUL]".toByteArray(StandardCharsets.UTF_8),
            "[SOH]".toByteArray(StandardCharsets.UTF_8),
            "[STX]".toByteArray(StandardCharsets.UTF_8),
            "[ETX]".toByteArray(StandardCharsets.UTF_8),
            "[EOT]".toByteArray(StandardCharsets.UTF_8),
            "[ENQ]".toByteArray(StandardCharsets.UTF_8),
            "[ACK]".toByteArray(StandardCharsets.UTF_8),
            "[BEL]".toByteArray(StandardCharsets.UTF_8),
            "[BS]".toByteArray(StandardCharsets.UTF_8),
            null,  // TAB
            null,  // LF
            "[VT]".toByteArray(StandardCharsets.UTF_8),
            "[FF]".toByteArray(StandardCharsets.UTF_8),
            null,  // CR
            "[SO]".toByteArray(StandardCharsets.UTF_8),
            "[SI]".toByteArray(StandardCharsets.UTF_8),
            "[DLE]".toByteArray(StandardCharsets.UTF_8),
            "[DC1]".toByteArray(StandardCharsets.UTF_8),
            "[DC2]".toByteArray(StandardCharsets.UTF_8),
            "[DC3]".toByteArray(StandardCharsets.UTF_8),
            "[DC4]".toByteArray(StandardCharsets.UTF_8),
            "[NAK]".toByteArray(StandardCharsets.UTF_8),
            "[SYN]".toByteArray(StandardCharsets.UTF_8),
            "[ETB]".toByteArray(StandardCharsets.UTF_8),
            "[CAN]".toByteArray(StandardCharsets.UTF_8),
            "[EM]".toByteArray(StandardCharsets.UTF_8),
            "[SUB]".toByteArray(StandardCharsets.UTF_8),
            "[ESC]".toByteArray(StandardCharsets.UTF_8),
            "[FS]".toByteArray(StandardCharsets.UTF_8),
            "[GS]".toByteArray(StandardCharsets.UTF_8),
            "[RS]".toByteArray(StandardCharsets.UTF_8),
            "[US]".toByteArray(StandardCharsets.UTF_8)
        )
        private const val SPACE = ' '

        @Volatile
        private var INSTANCE: LogsManager? = null
        private val SPACE_AFTER_TIME = byteArrayOf(0)
        private const val INITIAL_INFO_PREFIX = "!"
        fun init(name: String, ctx: Context) {
            if (INSTANCE == null) {
                synchronized(LogsManager::class.java) {
                    if (INSTANCE == null) {
                        val instance = LogsManager(name, ctx)
                        val properties = System.getProperties()
                        val builder = ByteArrayBuilder.builder()
                        builder.add(INITIAL_INFO_PREFIX)
                        for (e in properties.stringPropertyNames()) {
                            val property = properties.getProperty(e)
                            if (property != null && !property.isEmpty()) {
                                builder.add("$e: $property\n")
                            }
                        }
                        instance.writer!!.log(builder.build())
                        INSTANCE = instance
                    }
                }
            }
        }

        @get:Throws(IllegalStateException::class)
        val instance: LogsManager
            get() {
                checkNotNull(INSTANCE) { "Not initialised" }
                return INSTANCE!!
            }

        @Synchronized
        fun shutdown() {
            try {
                shutdown(false)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }

        @Synchronized
        @Throws(InterruptedException::class)
        fun shutdown(blocking: Boolean) {
            if (INSTANCE != null) {
                INSTANCE!!.writer!!.flush()
                if (blocking) {
                    INSTANCE!!.writer!!.join()
                }
                INSTANCE = null
            }
        }

        private fun order(level: String): Int {
            when (level) {
                Level.TRACE -> return 0
                Level.DEBUG -> return 1
                Level.INFO -> return 2
                Level.WARNING -> return 3
                ERROR -> return 4
                Level.FATAL -> return 5
            }
            throw IllegalArgumentException("Not a Level")
        }

        private fun order(level: Int): String {
            when (level) {
                0 -> return Level.TRACE
                1 -> return Level.DEBUG
                2 -> return Level.INFO
                3 -> return Level.WARNING
                4 -> return ERROR
                5 -> return Level.FATAL
            }
            throw IllegalArgumentException("Not a Level")
        }

        /**
         * Replace all symbols from first 32 symbols in ASCII except `CR` (`\r`), `LF` (`\n`) and `TAB` (`\t`) with
         * its names enclosed in square
         * brackets.
         *
         * @param victim potentially dangerous array
         * @return ASCII safe byte array
         * @see .REPLACEMENTS
         */
        private fun escapeControls(victim: ByteArray): ByteArray {
            var res: ByteArrayBuilder? = null
            var start = 0
            for (i in victim.indices) {
                if (victim[i] < REPLACEMENTS.size && victim[i] >= 0 && REPLACEMENTS[victim[i].toInt()] != null) {
                    if (res == null) {
                        res = ByteArrayBuilder.builder()
                    }
                    if (start < i) {
                        res!!.add(Arrays.copyOfRange(victim, start, i))
                    }
                    res!!.add(REPLACEMENTS[victim[i].toInt()])
                    start = i + 1
                }
            }
            return if (res == null) victim else (if (start < victim.size) res.add(
                Arrays.copyOfRange(
                    victim,
                    start,
                    victim.size
                )
            ) else res).build()
        }
    }

    init {
        try {
            writer = WriterThread(name, ctx)
            writer!!.start()
        } catch (e: IOException) {
            Logger.e(javaClass.simpleName, "Sadly, logging cannot start: $e")
            throw RuntimeException(e)
        }
    }
}