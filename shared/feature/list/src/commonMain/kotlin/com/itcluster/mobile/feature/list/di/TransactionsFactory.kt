package com.itcluster.mobile.feature.list.di

import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.presentation.TransactionsVm

class TransactionsFactory(
    private val authStore: AuthStore
) {

    fun createTransactionsModel(billId: Long): TransactionsVm =
        TransactionsVm(
            authStore = authStore
        ).apply {
            onCreated(billId)
        }
}