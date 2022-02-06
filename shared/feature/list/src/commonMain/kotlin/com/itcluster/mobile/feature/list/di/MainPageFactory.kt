package com.itcluster.mobile.feature.list.di

import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.presentation.MainPageViewModel

class MainPageFactory (
    private val authStore: AuthStore
//    private val listSource: ListSource<T>,
//    private val strings: MainPageViewModel.Strings,
//    private val unitsFactory: MainPageViewModel.UnitsFactory<T>
) {

    fun createMainPageModel(): MainPageViewModel {
        return MainPageViewModel(
            authStore = authStore
//            listSource = listSource,
//            strings = strings,
//            unitsFactory = unitsFactory
        ).apply {
            onCreated()
        }
    }
}