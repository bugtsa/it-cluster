package com.itcluster.mobile.feature.list.di

import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.model.CompaniesStore
import com.itcluster.mobile.feature.list.presentation.CompaniesVm

class CompaniesFactory(
    private val companiesStore: CompaniesStore,
    private val authStore: AuthStore
) {
    fun createCompaniesViewModel() =
        CompaniesVm(
            companiesStore,
            authStore
        ).apply {
            fetchData()
        }
}