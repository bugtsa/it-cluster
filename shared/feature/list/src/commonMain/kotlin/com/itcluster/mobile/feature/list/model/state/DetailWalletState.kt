package com.itcluster.mobile.feature.list.model.state

import com.itcluster.mobile.presentation.models.WalletItemModel
import com.itcluster.mobile.presentation.models.WalletTransactionModel

sealed class DetailWalletState {
    object NoState: DetailWalletState()

    class SuccessTransactions(
        val transactions: List<WalletTransactionModel>
    ): DetailWalletState()

    class SuccessDetail(val details: WalletItemModel): DetailWalletState()

    sealed class Error : DetailWalletState() {

        class Unknown(
            val message: String,
            val throwable: Throwable
        ) : Error()
    }
}