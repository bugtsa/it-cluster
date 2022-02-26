package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.presentation.models.CurrencyModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyDetailWalletRes(

    val decimals: Int,

    @SerialName("id")
    val id: Int,
) {

    companion object {

        fun CurrencyDetailWalletRes.toModel(): CurrencyModel.CurrencyOfDetailWalletModel =
            CurrencyModel.CurrencyOfDetailWalletModel(
                idWallet = id,
                decimals = decimals
            )
    }
}