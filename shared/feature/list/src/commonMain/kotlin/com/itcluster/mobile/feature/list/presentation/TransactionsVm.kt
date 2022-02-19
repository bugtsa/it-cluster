package com.itcluster.mobile.feature.list.presentation

import com.itcluster.mobile.domain.network.ItClusterSDK
import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.model.state.TransactionsState
import com.itcluster.mobile.feature.list.model.state.WalletState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TransactionsVm(
    private val authStore: AuthStore
) : ViewModel() {

    private val sdk: ItClusterSDK by lazy { ItClusterSDK() }

    private val mainScope = MainScope()

    val stateTransactions: LiveData<TransactionsState> get() = _stateTransactions

    private val _stateTransactions: MutableLiveData<TransactionsState> = MutableLiveData(TransactionsState.NoState)

    fun onCreated(billId: Long) {
        loadTransactionsList(billId, PAGE_START_DEFAULT)
    }

    private fun loadTransactionsList(billId: Long, page: Int) {
        authStore.accessToken?.also { authToken ->
            mainScope.launch {
                kotlin.runCatching {
                    sdk.walletTransactions(authToken, billId.toString(), page.toString())
                }.onSuccess {
                    _stateTransactions.value = TransactionsState.SuccessTransactions(it.list)
                }.onFailure { throwable ->
                    _stateTransactions.value = TransactionsState.Error.Unknown(
                        "Неизвестная ошибка. Обратитесь к разработчику",
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