package com.itcluster.mobile.app.ext.log

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.itcluster.mobile.app.BuildConfig
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.charset.StandardCharsets
import java.util.*

class WriterThread internal constructor(logFile: String?, ctx: Context) :
    Thread("Log writer thread") {
    private val queue: Queue<ByteArray> = LinkedList()
    private val logFile: RandomAccessFile = RandomAccessFile(logFile, "rwd")
    private val prefs: SharedPreferences

    @Volatile
    private var logSize = BuildConfig.SEND_AS_LOG_FILE_SIZE

    @Volatile
    private var newSize = false

    @Volatile
    private var flushing = false

    /**
     * Writes log messages into file. Older logs will be overwritten by new ones. Each log message starts with `STX` symbol and ends with `ETX` symbol. Last point of
     * concatenation for separated message marked with `RS` symbol.
     */
    @SuppressLint("ApplySharedPref")
    override fun run() {
        while (!isInterrupted || flushing) {
            var e: ByteArray? = null
            synchronized(queue) {
                if (queue.isEmpty()) {
                    if (flushing) {
                        return
                    }
                    try {
                        (queue as Object).wait()
                    } catch (err: InterruptedException) {
                        return
                    }
                } else {
                    e = queue.poll()
                }
            }
            if (e == null || e!!.size == 0) {
                continue
            }
            val newPtr: Long
            synchronized(logFile) {
                try {
                    val size: Long
                    synchronized(this) {
                        if (newSize) {
                            checkAndSetLength()
                        }
                        size = logSize
                    }
                    run {
                        if (logFile.filePointer + 1 > size) {
                            Log.e(
                                "WriterThread",
                                "run: strange file pointer position " + logFile.filePointer + "/" + size
                            )
                            logFile.seek(size - 1)
                        }
                        if (logFile.filePointer + 1 == size) {
                            logFile.write(RS.toInt())
                            logFile.seek(0)
                        }
                    }
                    logFile.write(STX.toInt())
                    val filePointer = logFile.filePointer
                    if (filePointer + e!!.size + 1 > size) {
                        logFile.write(e, 0, (size - filePointer - 1).toInt())
                        logFile.write(RS.toInt()) // RS, marks end of log (in case of changing log length)
                        logFile.seek(0)
                        logFile.write(
                            e,
                            (size - filePointer - 1).toInt(),
                            (filePointer + e!!.size - size + 1).toInt()
                        )
                    } else {
                        logFile.write(e)
                    }
                    logFile.write(ETX.toInt())
                    newPtr = logFile.filePointer
                } catch (err: IOException) {
                    Log.e("VTB logger", "run: logging failed", err)
                    return
                }
                prefs
                    .edit()
                    .putLong(SP_PTR, newPtr)
                    .commit()
            }
        }
    }

    /**
     * Add some bytes to logging queue. It is usually fast, since actual log write will be performed on this thread. Please, avoid using bytes `RS (0x1e)`, `STX (0x02)`
     * and `ETX (0x03)`.
     *
     * @param bytes what you want to log.
     *
     * @throws {@see IllegalArgumentException} when `bytes.length` is dangerously close to or even bigger than log size.
     * @see .setLogSize
     */
    fun log(bytes: ByteArray) {
        require(bytes.size + 2 < logSize) { "You tried to log " + bytes.size + " bytes, but log size is " + logSize + " bytes. Ignored." }
        synchronized(queue) {
            queue.add(bytes)
            if (BuildConfig.DEBUG) {
                for (i in bytes.indices) {
                    require(bytes[i] != RS) {
                        """
                        found `RS' on position $i
                        ${Arrays.toString(bytes)}
                        
                        """.trimIndent() + String(
                            bytes,
                            StandardCharsets.UTF_8
                        )
                    }
                }
            }
            (queue as Object).notify()
        }
    }

    /**
     * Change log size on fly. Note that changes doesn't applies immediately, but this call ensure that you will not lose last `newSize` bytes
     * logged **after** this call.
     *
     * @param newSize desired size of logs.
     */
    @Synchronized
    fun setLogSize(newSize: Long) {
        logSize = newSize
        this.newSize = true
    }

    /**
     * Reads log file in list of byte arrays. It extracts records between `STX` and `ETX`.
     *
     * @return list of logged byte arrays.
     *
     * @throws IOException when I/O error occurs.
     */
    @Throws(IOException::class)
    fun readLogFile(): List<ByteArray> {
        val result: MutableList<ByteArray> = ArrayList()
        synchronized(logFile) {
            val size = logFile.length()
            val savedPtr = logFile.filePointer
            logFile.seek(0)
            var readed: Long = 0
            while (readed < size) {
                val buf = ByteArray(4096)
                readed += logFile.read(buf, 0, 4096).toLong()
                result.add(buf)
            }
        }
        return result
    }

    /**
     * Notify that this thread should finish all writing operations and
     * **do not** wait for next [.log] call.
     * After this call you can safely [.join] and when log queue will became empty this thread will finish.
     */
    fun flush() {
        flushing = true
        interrupt()
    }

    @Throws(IOException::class)
    private fun checkAndSetLength() {
        synchronized(logFile) {
            synchronized(this) {
                if (logFile.length() != logSize) {
                    logFile.setLength(logSize + 1)
                    val fp = logFile.filePointer
                    if (fp == logSize + 1 || fp == logSize) {
                        logFile.seek(logSize - 1)
                    }
                    newSize = false
                }
            }
        }
    }

    companion object {
        const val STX: Byte = 0x2
        const val ETX: Byte = 0x3
        const val RS: Byte = 0x1e
        private const val LOG_SP_NAME = "log_prefs"
        private const val SP_PTR = "ptr"
        private fun expand(buffer: ByteArray, len: Int): ByteArray {
            val buffer2 = ByteArray(2 * buffer.size)
            System.arraycopy(buffer, 0, buffer2, 0, len)
            return buffer2
        }
    }

    init {
        checkAndSetLength()
        prefs = ctx.getSharedPreferences(LOG_SP_NAME, Context.MODE_PRIVATE)
        this.logFile.seek(prefs.getLong(SP_PTR, 0))
    }
}