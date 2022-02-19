package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.domain.Constants.Companion.INT_DEFAULT
import com.itcluster.mobile.presentation.models.WalletTransactionModel
import kotlinx.serialization.Serializable

@Serializable
data class TransactionWalletRes(

	val transactionId: Int?,

	val datetime: Int,

	val amount: Int,

	val comment: String,

	val id: Int,

	val type: Int,

	val hash: String
) {

	companion object {

		fun TransactionWalletRes.toModel() = WalletTransactionModel(
			this.transactionId ?: INT_DEFAULT,
			this.datetime,
			this.amount,
			this.comment,
			this.id,
			this.type,
			this.hash
		)
	}
}


