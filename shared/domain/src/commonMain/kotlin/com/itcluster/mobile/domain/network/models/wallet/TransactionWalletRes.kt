package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.domain.Constants.Companion.INT_DEFAULT
import com.itcluster.mobile.domain.network.models.wallet.WalletExt.correctAmount
import com.itcluster.mobile.presentation.models.WalletTransactionModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionWalletRes(

	val transactionId: Int?,

	val datetime: Int,

	val amount: Int,

	@SerialName("amount_decimals")
	val amountDecimals: Int,

	val comment: String,

	val id: Int,

	val type: Int,

	val hash: String
) {

	companion object {

		fun TransactionWalletRes.toModel() = WalletTransactionModel(
			this.transactionId ?: INT_DEFAULT,
			this.datetime,
			correctAmount(this.amount.toString(), this.amountDecimals),
			this.comment,
			this.id,
			this.type,
			this.hash
		)
	}
}


