package com.itcluster.mobile.feature.list.di

import com.itcluster.mobile.feature.list.model.ListSource
import com.itcluster.mobile.feature.list.presentation.MainPageViewModel

class MainPageFactory<T> (
    private val listSource: ListSource<T>,
    private val strings: MainPageViewModel.Strings,
    private val unitsFactory: MainPageViewModel.UnitsFactory<T>
) {

//    fun createMainPageModel(): MainPageViewModel<T> {
//        return MainPageViewModel(
//            listSource = listSource,
//            strings = strings,
//            unitsFactory = unitsFactory
//        )
//    }
}