package com.itcluster.mobile.feature.list.di

import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.presentation.WalletsVm

class WalletsFactory(
    private val authStore: AuthStore
) {

    fun createWalletsModel(): WalletsVm = WalletsVm(
        authStore = authStore
    ).apply {
        onCreated()
    }
}