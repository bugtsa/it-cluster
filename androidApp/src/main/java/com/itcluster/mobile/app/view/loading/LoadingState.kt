package com.itcluster.mobile.app.view.loading

sealed class LoadingState {

    object Filled : LoadingState()
    object Loading : LoadingState()
    object None : LoadingState()
    object Transparent : LoadingState()
}