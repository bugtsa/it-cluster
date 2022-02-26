package com.itcluster.mobile.feature.list.presentation

import com.itcluster.mobile.domain.network.ItClusterSDK
import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.model.state.DetailWalletState
import com.itcluster.mobile.feature.list.presentation.Constants.UNKNOWN_NETWORK_ERROR
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class WalletDetailVm(
    private val authStore: AuthStore
) : ViewModel() {

    private val sdk: ItClusterSDK by lazy { ItClusterSDK() }

    private val mainScope = MainScope()

    val stateDetailWallet: LiveData<DetailWalletState> get() = _stateDetailWallet

    private val _stateDetailWallet: MutableLiveData<DetailWalletState> = MutableLiveData(DetailWalletState.NoState)

    fun onCreated(billId: Long) {
        loadTransactionsList(billId, PAGE_START_DEFAULT)
        loadWalletDetail(billId)
    }

    private fun loadWalletDetail(billId: Long) {
        authStore.accessToken?.also { authToken ->
            mainScope.launch {
                kotlin.runCatching {
                    sdk.walletItem(authToken, billId.toString())
                }.onSuccess {
                    _stateDetailWallet.value = DetailWalletState.SuccessDetail(it)
                }.onFailure { throwable ->
                    _stateDetailWallet.value = DetailWalletState.Error.Unknown(
                        UNKNOWN_NETWORK_ERROR,
                        throwable
                    )
                }
            }
        }
    }

    private fun loadTransactionsList(billId: Long, page: Int) {
        authStore.accessToken?.also { authToken ->
            mainScope.launch {
                kotlin.runCatching {
                    sdk.walletTransactions(authToken, billId.toString(), page.toString())
                }.onSuccess {
                    _stateDetailWallet.value = DetailWalletState.SuccessTransactions(it.list)
                }.onFailure { throwable ->
                    _stateDetailWallet.value = DetailWalletState.Error.Unknown(
                        UNKNOWN_NETWORK_ERROR,
                        throwable
                    )
                }
            }
        }
    }

    companion object {

        private const val PAGE_START_DEFAULT = 1
    }
}