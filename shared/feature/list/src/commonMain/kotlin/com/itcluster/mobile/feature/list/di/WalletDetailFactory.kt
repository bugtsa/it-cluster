package com.itcluster.mobile.feature.list.di

import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.presentation.WalletDetailVm

class WalletDetailFactory(
    private val authStore: AuthStore
) {

    fun createWalletDetailModel(billId: Long): WalletDetailVm =
        WalletDetailVm(
            authStore = authStore
        ).apply {
            onCreated(billId)
        }
}