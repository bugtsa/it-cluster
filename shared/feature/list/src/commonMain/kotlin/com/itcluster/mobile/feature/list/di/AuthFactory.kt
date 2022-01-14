package com.itcluster.mobile.feature.list.di

import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.presentation.AuthVm

class AuthFactory(
    private val authStore: AuthStore
) {
    fun createAuthViewModel() =
        AuthVm(
            authStore
        )
}