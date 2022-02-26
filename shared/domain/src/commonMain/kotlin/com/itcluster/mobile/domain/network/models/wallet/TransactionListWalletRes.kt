package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.domain.network.models.wallet.PagesRes.Companion.toModel
import com.itcluster.mobile.domain.network.models.wallet.TransactionWalletRes.Companion.toModel
import com.itcluster.mobile.presentation.models.WalletTransactionsModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionListWalletRes(

    @SerialName("list")
    val list: List<TransactionWalletRes>,

    @SerialName("pages")
    val pages: PagesRes

) {

    companion object {

        fun TransactionListWalletRes.toModel(): WalletTransactionsModel =
            WalletTransactionsModel(
                this.pages.toModel(),
                this.list.map { it.toModel() }
            )
    }
}