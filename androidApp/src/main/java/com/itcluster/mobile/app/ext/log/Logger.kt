package com.itcluster.mobile.app.ext.log

import androidx.annotation.Size
import timber.log.Timber

object Logger {
    fun e(@Size(min = 3, max = 23) tag: String?, message: String?) {
        Timber.tag(tag!!).e(message)
    }

    fun i(@Size(min = 3, max = 23) tag: String?, message: String?) {
        Timber.tag(tag!!).i(message)
    }

    fun d(@Size(min = 3, max = 23) tag: String?, message: String?) {
        Timber.tag(tag!!).d(message)
    }

    fun v(@Size(min = 3, max = 23) tag: String?, message: String?) {
        Timber.tag(tag!!).v(message)
    }

    fun w(@Size(min = 3, max = 23) tag: String?, message: String?) {
        Timber.tag(tag!!).w(message)
    }
}