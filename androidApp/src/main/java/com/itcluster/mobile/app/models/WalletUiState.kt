package com.itcluster.mobile.app.models

import android.os.Parcelable
import com.itcluster.mobile.app.ext.recycler.RecyclerViewItem
import com.itcluster.mobile.presentation.models.WalletModel
import kotlinx.android.parcel.Parcelize

@Parcelize
class WalletUiState(
    val image: String,
    val name: String,
    val amount: String,
    val codeCurrency: String,
    val id: Long
): RecyclerViewItem, Parcelable {

    companion object {

        fun WalletModel.toState() : WalletUiState =
            WalletUiState(
                currency.image, name, amount, currency.code, id
            )
    }

    override fun getId() = id
}