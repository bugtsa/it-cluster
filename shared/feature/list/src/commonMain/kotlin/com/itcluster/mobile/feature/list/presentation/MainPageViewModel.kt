package com.itcluster.mobile.feature.list.presentation

import com.itcluster.mobile.domain.network.ItClusterSDK
import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.model.state.WalletState
import dev.icerock.moko.mvvm.livedata.*
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val authStore: AuthStore
) : ViewModel() {

    private val sdk: ItClusterSDK by lazy { ItClusterSDK() }

    private val mainScope = MainScope()

    val stateWallet: LiveData<WalletState> get() = _stateWallet

    private val _stateWallet: MutableLiveData<WalletState> = MutableLiveData(WalletState.NoState)

    fun onCreated() {
        loadWalletList()
    }

    private fun loadWalletList() {
        authStore.accessToken?.also { authToken ->
            mainScope.launch {
                kotlin.runCatching {
                    sdk.walletList(authToken)
                }.onSuccess {
                    _stateWallet.value = WalletState.SuccessWallet(it)
                }.onFailure {
                    val dd = it
                }
            }
        }
    }
}