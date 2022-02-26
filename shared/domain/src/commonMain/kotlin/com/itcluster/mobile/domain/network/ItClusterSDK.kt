package com.itcluster.mobile.domain.network

import com.itcluster.mobile.domain.network.models.auth.AuthRes
import com.itcluster.mobile.domain.network.models.auth.CompaniesRes.Companion.toModel
import com.itcluster.mobile.domain.network.models.auth.LoginReq
import com.itcluster.mobile.domain.network.models.wallet.TransactionListWalletRes.Companion.toModel
import com.itcluster.mobile.domain.network.models.wallet.WalletDetailParentRes.Companion.toModel
import com.itcluster.mobile.domain.network.models.wallet.WalletTransactionRes.Companion.toModel
import com.itcluster.mobile.presentation.models.CompanyModel
import com.itcluster.mobile.presentation.models.WalletItemModel
import com.itcluster.mobile.presentation.models.WalletModel
import com.itcluster.mobile.presentation.models.WalletTransactionsModel

class ItClusterSDK {

    private val api = ItClusterApi()

    @Throws(Exception::class)
    suspend fun companiesRequest(req: LoginReq): List<CompanyModel> =
        api.companiesList(req).toModel()

    @Throws(Exception::class)
    suspend fun authTokenRequest(req: LoginReq): AuthRes =
        api.authToken(req)

    @Throws(Exception::class)
    suspend fun walletList(authToken: String): List<WalletModel.WalletFullModel> =
        api.walletList(authToken).toModel()

    @Throws(Exception::class)
    suspend fun walletItem(authToken: String, billId: String): WalletItemModel =
        api.walletDetails(authToken, billId).toModel()

    @Throws(Exception::class)
    suspend fun walletTransactions(
        authToken: String, billId: String,
        page: String
    ): WalletTransactionsModel =
        api.walletTransactions(authToken, billId, page).toModel()
}