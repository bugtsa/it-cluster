package com.itcluster.mobile.domain.network

import com.itcluster.mobile.domain.network.models.auth.AuthRes
import com.itcluster.mobile.domain.network.models.auth.LoginReq

class ItClusterSDK {

    private val api = ItClusterApi()

    @Throws(Exception::class)
    suspend fun requestAuth(req: LoginReq): AuthRes =
        api.postAuth(req)

}