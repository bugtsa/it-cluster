package com.itcluster.mobile.presentation.models

sealed class CurrencyModel(
    val id: Int
) {

    class CurrencyOfListModel(
        val image: String,
        val decimals: Int,
        val idTran: Int,
        val code: String
    ) : CurrencyModel(idTran)

    class CurrencyOfBillModel(
        val image: String,
        val idBill: Int,
        val code: String
    ) : CurrencyModel(idBill)

    class CurrencyOfDetailWalletModel(
        val decimals: Int,
        val idWallet: Int
    ) : CurrencyModel(idWallet)
}

