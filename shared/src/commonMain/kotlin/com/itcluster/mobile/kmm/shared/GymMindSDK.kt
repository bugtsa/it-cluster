package com.itcluster.mobile.kmm.shared

import com.itcluster.mobile.kmm.shared.cache.Database
import com.itcluster.mobile.kmm.shared.cache.DatabaseDriverFactory
import com.itcluster.mobile.kmm.shared.network.AppSocket

class GymMindSDK(
    databaseDriverFactory: DatabaseDriverFactory,
    appSocket: AppSocket
) {
    private val database = Database(databaseDriverFactory)
    private val socket = appSocket

    fun getSocket() = socket
}
