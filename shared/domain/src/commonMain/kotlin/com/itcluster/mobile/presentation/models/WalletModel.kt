package com.itcluster.mobile.presentation.models

sealed class WalletModel {

    class WalletFullModel(
        val id: Long,
        val currency: CurrencyModel.CurrencyOfListModel,
        val amount: String,
        val name: String,
        val isDefault: Int,
        val cardId: Long
    ) : WalletModel()

    class WalletMediumModel(
        val id: Long,
        val currency: CurrencyModel.CurrencyOfBillModel,
        val name: String,
        val isDefault: Int,
        val cardId: Long
    ) : WalletModel()

    class WalletShortModel(
        val id: Long,
        val currency: CurrencyModel.CurrencyOfDetailWalletModel,
        val amount: String
    ) : WalletModel()
}