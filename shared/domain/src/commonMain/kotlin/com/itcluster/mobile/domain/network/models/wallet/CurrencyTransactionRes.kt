package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.presentation.models.CurrencyModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyTransactionRes(

	val image: String,

	val decimals: Int,

	@SerialName("id")
	val id: Int,

	val code: String
) {

	companion object {

		fun CurrencyTransactionRes.toModel(): CurrencyModel = CurrencyModel.CurrencyOfListModel(image, decimals, id, code)
	}
}