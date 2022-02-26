package com.itcluster.mobile.app.models

import android.os.Parcelable
import com.itcluster.mobile.app.ext.recycler.RecyclerViewItem
import com.itcluster.mobile.app.models.TypeUiState.Companion.toTypeUiStase
import com.itcluster.mobile.domain.Constants.Companion.EMPTY_SEPARATOR
import com.itcluster.mobile.presentation.models.WalletTransactionModel
import kotlinx.parcelize.Parcelize

@Parcelize
class TransactionUIState(
    val id: Long,
    val type: TypeUiState,
    val comment: String,
    val amount: String,
    val codeCurrency: String,
    val dateTime: String
) : RecyclerViewItem, Parcelable {

    override fun getId() = id

    companion object {


        fun WalletTransactionModel.toState(): TransactionUIState =
            TransactionUIState(
                this.id,
                this.type.toTypeUiStase(),
                this.comment,
                this.amount,
                EMPTY_SEPARATOR,
                this.datetime.toString()
            )
    }
}