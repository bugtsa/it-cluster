package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.domain.Constants.Companion.LONG_DEFAULT
import com.itcluster.mobile.domain.network.models.wallet.CurrencyRes.Companion.toModel
import com.itcluster.mobile.domain.network.models.wallet.WalletExt.correctAmount
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

		fun List<WalletRes>.toModel(): List<WalletModel> =
			map { res ->
				val currency = res.currency.toModel()
				WalletModel(
					correctAmount(res.amount, currency.decimals),
					res.name,
					currency,
					res.id,
					res.isDefault,
					res.cardId ?: LONG_DEFAULT
				)
			}
	}
}