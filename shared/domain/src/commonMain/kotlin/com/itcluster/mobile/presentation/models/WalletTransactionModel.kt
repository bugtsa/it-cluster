package com.itcluster.mobile.presentation.models

class WalletTransactionModel(
    val transactionId: Int,

    val datetime: Int,

    val amount: String,

    val comment: String,

    val id: Int,

    val type: Int,

    val hash: String
)