package com.itcluster.mobile.feature.list.di

import com.itcluster.mobile.feature.list.model.ListSource
import com.itcluster.mobile.feature.list.presentation.ListViewModel

class ListFactory<T>(
    private val listSource: ListSource<T>,
    private val strings: ListViewModel.Strings,
    private val unitsFactory: ListViewModel.UnitsFactory<T>
) {
    fun createListViewModel(): ListViewModel<T> {
        return ListViewModel(
            listSource = listSource,
            strings = strings,
            unitsFactory = unitsFactory
        )
    }
}
