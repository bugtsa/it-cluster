package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.domain.Constants.Companion.LONG_DEFAULT
import com.itcluster.mobile.domain.network.models.wallet.CurrencyTransactionRes.Companion.toModel
import com.itcluster.mobile.domain.network.models.wallet.WalletExt.correctAmount
import com.itcluster.mobile.presentation.models.CurrencyModel
import com.itcluster.mobile.presentation.models.WalletModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletTransactionRes(

	val amount: String,

	val name: String,

	@SerialName("currency")
	val currency: CurrencyTransactionRes,

	val id: Long,

	@SerialName("is_default")
	val isDefault: Int,

	@SerialName("card_id")
	val cardId: Long?
) {

	companion object {

		fun List<WalletTransactionRes>.toModel(): List<WalletModel.WalletFullModel> =
			map { res ->
				val currency = res.currency.toModel() as CurrencyModel.CurrencyOfListModel
				WalletModel.WalletFullModel(
					id = res.id,
					currency = currency,
					amount = correctAmount(res.amount, currency.decimals),
					name = res.name,
					isDefault = res.isDefault,
					cardId = res.cardId ?: LONG_DEFAULT
				)
			}
	}
}