package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.domain.Constants.Companion.EMPTY_SEPARATOR
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
	val cardId: String?
) {

	companion object {

		fun List<WalletRes>.toModel(): List<WalletModel> =
			map {
				WalletModel(
					it.amount,
					it.name,
					it.currency.toModel(),
					it.id,
					it.isDefault,
					it.cardId ?: EMPTY_SEPARATOR
				)
			}
	}
}