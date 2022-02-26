package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.domain.network.models.wallet.CurrencyDetailWalletRes.Companion.toModel
import com.itcluster.mobile.presentation.models.WalletModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletDetailRes(

	@SerialName("amount_decimals")
	val amountDecimals: String,

	val amount: String,

	val currency: CurrencyDetailWalletRes,

	val id: Long
) {

	companion object {

		fun WalletDetailRes.toModel(): WalletModel.WalletShortModel =
			WalletModel.WalletShortModel(
				id,
				currency.toModel(),
				amountDecimals
			)
	}
}