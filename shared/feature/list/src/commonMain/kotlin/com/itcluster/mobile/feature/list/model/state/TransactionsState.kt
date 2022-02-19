package com.itcluster.mobile.feature.list.model.state

import com.itcluster.mobile.presentation.models.WalletTransactionModel

sealed class TransactionsState {
    object NoState: TransactionsState()

    class SuccessTransactions(
        val transactions: List<WalletTransactionModel>
    ): TransactionsState()

    sealed class Error : TransactionsState() {

        class Unknown(
            val message: String,
            val throwable: Throwable
        ) : Error()
    }
}