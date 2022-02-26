package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.presentation.models.CurrencyModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyBillRes(

    val image: String,

    @SerialName("id")
    val id: Int,

    val code: String
) {

    companion object {

        fun CurrencyBillRes.toModel(): CurrencyModel.CurrencyOfBillModel =
            CurrencyModel.CurrencyOfBillModel(image, id, code)
    }
}