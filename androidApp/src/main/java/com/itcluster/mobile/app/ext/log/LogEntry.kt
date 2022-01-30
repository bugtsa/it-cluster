package com.itcluster.mobile.app.ext.log

import com.itcluster.mobile.app.ext.log.Level.Companion.TRACE

/**
 * Дата класс представляющий собой контейнер для логируемой информации
 */
data class LogEntry internal constructor(
    val time: Long,
    val name: String,
    val level: String,
    val message: String?,
    val error: Throwable?
) {

    companion object {
        @JvmStatic
        fun builder() = LogEntryBuilder()
    }

    class LogEntryBuilder {
        private var error: Throwable? = null
        private var level: String = TRACE
        private var message: String? = null
        private var name: String = ""

        fun error(error: Throwable?) = apply { this.error = error }

        fun level(level: String) = apply { this.level = level }

        fun message(message: String?) = apply { this.message = message }

        fun name(name: String) = apply { this.name = name }

        fun build() = LogEntry(
            time = System.currentTimeMillis(),
            name = name,
            level = level,
            message = message,
            error = error
        )
    }
}