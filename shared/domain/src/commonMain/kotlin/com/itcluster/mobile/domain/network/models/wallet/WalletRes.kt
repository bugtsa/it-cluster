package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.domain.Constants.Companion.LONG_DEFAULT
import com.itcluster.mobile.domain.network.models.wallet.CurrencyRes.Companion.toModel
import com.itcluster.mobile.presentation.models.WalletModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletRes(

	val amount: String,

	val name: String,

	@SerialName("currency")
	val currency: CurrencyRes,

	val id: Long,

	@SerialName("is_default")
	val isDefault: Int,

	@SerialName("card_id")
	val cardId: Long?
) {

	companion object {

		private fun String.addCharAtIndex(char: Char, index: Int) =
			StringBuilder(this).apply { insert(index, char) }.toString()

		fun List<WalletRes>.toModel(): List<WalletModel> =
			map { res ->
				val currency = res.currency.toModel()
				val newAmount = takeIf { res.amount.length > currency.decimals }?.let {
					val newString = res.amount.addCharAtIndex('.', res.amount.length - currency.decimals)
					newString
				} ?: res.amount
				WalletModel(
					newAmount,
					res.name,
					currency,
					res.id,
					res.isDefault,
					res.cardId ?: LONG_DEFAULT
				)
			}
	}
}