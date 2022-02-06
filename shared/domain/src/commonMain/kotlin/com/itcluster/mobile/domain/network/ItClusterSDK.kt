package com.itcluster.mobile.domain.network

import com.itcluster.mobile.domain.network.models.auth.AuthRes
import com.itcluster.mobile.domain.network.models.auth.CompaniesRes.Companion.toModel
import com.itcluster.mobile.domain.network.models.auth.LoginReq
import com.itcluster.mobile.domain.network.models.wallet.WalletRes.Companion.toModel
import com.itcluster.mobile.presentation.models.CompanyModel
import com.itcluster.mobile.presentation.models.WalletModel

class ItClusterSDK {

    private val api = ItClusterApi()

    @Throws(Exception::class)
    suspend fun companiesRequest(req: LoginReq): List<CompanyModel> =
        api.companiesList(req).toModel()

    @Throws(Exception::class)
    suspend fun authTokenRequest(req: LoginReq): AuthRes =
        api.authToken(req)

    @Throws(Exception::class)
    suspend fun walletList(authToken: String): List<WalletModel> =
        api.walletList(authToken).toModel()
}