package com.itcluster.mobile.feature.list.presentation

import com.itcluster.mobile.domain.network.ItClusterSDK
import com.itcluster.mobile.domain.network.models.auth.LoginReq
import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.model.state.WalletState
import dev.icerock.moko.mvvm.livedata.*
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.units.TableUnitItem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val authStore: AuthStore
) : ViewModel() {

    private val sdk: ItClusterSDK by lazy { ItClusterSDK() }

    private val mainScope = MainScope()

    val counter: LiveData<String> get() = _counter.map { it.toString() }
    val isSuccess: LiveData<WalletState> get() = _isSuccess

    private val _counter: MutableLiveData<Int> = MutableLiveData(0)
    private val _isSuccess: MutableLiveData<WalletState> = MutableLiveData(WalletState.NoState)

    fun onCounterButtonPressed() {
        val current = _counter.value
        _counter.value = current + 1
    }

    fun onCreated() {
        loadWalletList()
    }

    private fun loadWalletList() {
        authStore.accessToken?.also { authToken ->
            mainScope.launch {
                kotlin.runCatching {
                    sdk.walletList(authToken)
                }.onSuccess {
                    _isSuccess.value = WalletState.SuccessWallet(it)
                }.onFailure {
                    val dd = it
                }
            }
        }
    }
}