package com.itcluster.mobile.app.ext.log

/**
 * Точка расширения, позволяющая включить дополнительную информацию в результирующий архив логов для отправки
 */
interface ArchiveLogEntryProvider {

    /**
     * Имя файла в архиве
     */
    val logEntryName: String

    /**
     * Содержимое файла для включения в архив
     */
    val logEntryContent: ByteArray
}