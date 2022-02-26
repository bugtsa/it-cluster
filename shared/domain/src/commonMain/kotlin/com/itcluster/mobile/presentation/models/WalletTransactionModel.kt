package com.itcluster.mobile.presentation.models

class WalletTransactionModel(
    val transactionId: Int,

    val datetime: Long,

    val amount: String,

    val comment: String,

    val id: Long,

    val type: Int,

    val hash: String
)