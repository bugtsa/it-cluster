package com.itcluster.mobile.app.presentation.view.loading

sealed class LoadingState {

    object Filled : LoadingState()
    object Loading : LoadingState()
    object None : LoadingState()
    object Transparent : LoadingState()
}