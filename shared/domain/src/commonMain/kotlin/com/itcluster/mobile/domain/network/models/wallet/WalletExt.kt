package com.itcluster.mobile.domain.network.models.wallet

object WalletExt {

    fun correctAmount(amount: String, decimals: Int) =
        takeIf { amount.length > decimals }?.let {
            val newString = amount.addCharAtIndex('.', amount.length - decimals)
            newString
        } ?: amount

    private fun String.addCharAtIndex(char: Char, index: Int) =
        StringBuilder(this).apply { insert(index, char) }.toString()
}