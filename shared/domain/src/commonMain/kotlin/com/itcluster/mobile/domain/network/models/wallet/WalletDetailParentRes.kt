package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.domain.network.models.wallet.BillRes.Companion.toModel
import com.itcluster.mobile.domain.network.models.wallet.WalletDetailRes.Companion.toModel
import com.itcluster.mobile.presentation.models.WalletItemModel
import kotlinx.serialization.Serializable

@Serializable
data class WalletDetailParentRes(

	val wallet: WalletDetailRes,

	val bill: BillRes
) {

	companion object {

		fun WalletDetailParentRes.toModel(): WalletItemModel =
			WalletItemModel(
				bill.toModel(),
				wallet.toModel()
			)
	}
}