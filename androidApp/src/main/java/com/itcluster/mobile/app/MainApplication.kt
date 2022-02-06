package com.itcluster.mobile.app

import android.app.Application
import com.itcluster.mobile.app.ext.log.LogSniffer
import com.itcluster.mobile.app.ext.log.LogsManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        LogsManager.init(
            filesDir.absolutePath + "/app.log",
            this
        )
        LogSniffer.init(this)
    }
}
