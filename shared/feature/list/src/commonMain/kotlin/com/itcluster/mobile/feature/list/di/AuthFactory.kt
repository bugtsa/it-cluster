package com.itcluster.mobile.feature.list.di

import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.model.CompaniesStore
import com.itcluster.mobile.feature.list.presentation.AuthVm

class AuthFactory(
    private val authStore: AuthStore,
    private val companiesStore: CompaniesStore
) {
    fun createAuthViewModel() =
        AuthVm(
            authStore,
            companiesStore
        ).apply { onCreate() }
}