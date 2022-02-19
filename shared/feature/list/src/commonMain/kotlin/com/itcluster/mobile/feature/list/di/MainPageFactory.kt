package com.itcluster.mobile.feature.list.di

import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.presentation.MainPageVm

class MainPageFactory(
    private val authStore: AuthStore
) {

    fun createMainPageModel(): MainPageVm = MainPageVm(
        authStore = authStore
    ).apply {
        onCreated()
    }
}