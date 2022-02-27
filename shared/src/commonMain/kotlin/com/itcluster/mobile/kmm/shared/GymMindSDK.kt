package com.itcluster.mobile.kmm.shared

import com.itcluster.mobile.kmm.shared.network.AppSocket

class GymMindSDK(
    appSocket: AppSocket
) {
    private val socket = appSocket

    fun getSocket() = socket
}
