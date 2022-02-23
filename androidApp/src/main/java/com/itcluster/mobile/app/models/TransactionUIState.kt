package com.itcluster.mobile.app.models

import android.os.Parcelable
import com.itcluster.mobile.app.ext.recycler.RecyclerViewItem
import com.itcluster.mobile.presentation.models.WalletTransactionModel
import kotlinx.parcelize.Parcelize

@Parcelize
class TransactionUIState(
    val id: Int
) : RecyclerViewItem, Parcelable {

    override fun getId() = id

    companion object {

        fun WalletTransactionModel.toState(): TransactionUIState =
            TransactionUIState(
                this.id
            )
    }
}