package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.domain.Constants.Companion.LONG_DEFAULT
import com.itcluster.mobile.domain.network.models.wallet.CurrencyBillRes.Companion.toModel
import com.itcluster.mobile.presentation.models.WalletModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BillRes(

	val name: String,

	val currency: CurrencyBillRes,

	val id: Long,

	@SerialName("is_default")
	val isDefault: Int,

	@SerialName("card_id")
	val cardId: Long?
) {

	companion object {

		fun BillRes.toModel(): WalletModel.WalletMediumModel =
			WalletModel.WalletMediumModel(
				id = id,
				currency = currency.toModel(),
				name = name,
				isDefault = isDefault,
				cardId = cardId ?: LONG_DEFAULT
			)
	}
}