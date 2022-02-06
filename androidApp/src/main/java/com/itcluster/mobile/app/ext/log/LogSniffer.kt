package com.itcluster.mobile.app.ext.log

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Сборщик логов, который записывает их в файл и может отправить его в zip архиве в любое приложение
 *
 * @param context контекст
 */
class LogSniffer private constructor(
    private val context: Context
) {

    private val logFile = File(getDir(), "${getUnc()}$FILE_SUFFIX.log")
    private val zipFile = File(getDir(), "${getUnc()}$FILE_SUFFIX.zip")
    private val moneyRegex = """\d{3,}\.\d{1,2}[^a-zA-Z0-9]""".toRegex()
    private val md5 = MessageDigest.getInstance("MD5")

    private fun removeFiles() {
        getDir()?.let { dir ->
            dir.listFiles()?.forEach {
                if (it.name.contains(FILE_SUFFIX)) {
                    it.delete()
                }
            }
        }
    }

    private fun getUnc() = "UserIdRandom"

    private fun writeContent(content: String) {
        writeToFile(content)
    }

    private fun writeToFile(content: String) {
        if (!logFile.exists()) {
            logFile.createNewFile()
        }
        logFile.appendText("${obfuscate(content)}\n")
    }

    private fun zipFile(context: Context, providers: Array<out ArchiveLogEntryProvider>) {
        zipFile.run {
            createNewFile()
            outputStream().use { out ->
                ZipOutputStream(out).use { zip ->
                    if (logFile.exists()) {
                        logFile.inputStream().use { input ->
                            BufferedInputStream(input).use { log ->
                                zip.putNextEntry(ZipEntry("${getUnc()}.txt"))
                                log.copyTo(zip, 1024)
                                zip.closeEntry()
                            }
                        }
                    }
                    LogsManager.instance.createArchive(*providers)?.let { logsm ->
                        ByteArrayInputStream(logsm).use { log ->
                            zip.putNextEntry(ZipEntry("additionalLogs.zip"))
                            log.copyTo(zip)
                            zip.closeEntry()
                        }
                    }
                }
            }
        }
    }

    private fun getShareIntent(context: Context): Intent =
        Intent.createChooser(
            Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(
                    Intent.EXTRA_STREAM,
                    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", zipFile)
                ),
            "Поделиться"
        )

    private fun obfuscate(content: String): String {
        return content.replace(moneyRegex) { value ->
            md5.update(value.value.toByteArray(), 0, value.value.length)
            BigInteger(1, md5.digest()).toString(16)
        }
    }

    private fun getDir() = context.getExternalFilesDir(null)

    companion object {
        const val REQUEST_CODE = 23498

        private const val FILE_SUFFIX = "_ServerSequence"

        /**
         * Инстанс логгера
         */
        private var instance: LogSniffer? = null

        /**
         * Инициализация логгера
         *
         * @param context конетекст
         */
        @JvmStatic
        fun init(context: Context) =
            instance ?: LogSniffer(context).apply { instance = this }

        /**
         * Добавить лог
         *
         * @param request запрос
         */
        @JvmStatic
        fun addLog(request: String?) {
            request?.let { instance?.writeContent(it) }
        }

        /**
         * Запускает отправку логов
         *
         * @param activity активити
         */
        @JvmStatic
        fun sendLogs(activity: Activity, vararg providers: ArchiveLogEntryProvider) {
            instance?.run {
                zipFile(activity, providers)
                activity.startActivityForResult(getShareIntent(activity), REQUEST_CODE)
            }
        }

        /**
         * Удаление файлов
         */
        @JvmStatic
        fun clearFiles() {
            instance?.removeFiles()
        }
    }
}