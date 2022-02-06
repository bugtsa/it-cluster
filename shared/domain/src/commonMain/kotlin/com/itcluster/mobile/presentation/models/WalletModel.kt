package com.itcluster.mobile.presentation.models

class WalletModel(
    val amount: String,
    val name: String,
    val currency: CurrencyModel,
    val id: Int,
    val isDefault: Int,
    val cardId: String
)