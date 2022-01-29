package com.itcluster.mobile.feature.list.presentation

import com.itcluster.mobile.domain.network.ItClusterSDK
import com.itcluster.mobile.domain.network.api.errors.AuthErrorDto
import com.itcluster.mobile.domain.network.api.errors.ClusterException
import com.itcluster.mobile.domain.network.models.auth.LoginReq
import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.model.CompaniesStore
import com.itcluster.mobile.feature.list.model.state.LoginState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CompaniesVm(
    private val companiesStore: CompaniesStore,
    private val authStore: AuthStore
) : ViewModel(){

    private val sdk: ItClusterSDK by lazy { ItClusterSDK() }

    private val mainScope = MainScope()

    val state: LiveData<LoginState> get() = _state

    private val _state: MutableLiveData<LoginState> = MutableLiveData(LoginState.NoState)

    fun fetchData() {
        _state.value = LoginState.Companies.Data(companiesStore.companies)
    }

    fun authTokenRequest(companyId: Long) {
        val req = LoginReq(
            companiesStore.login,
            companiesStore.password,
            companyId
        )
        mainScope.launch {
            kotlin.runCatching {
                sdk.authTokenRequest(req)
            }.onSuccess { authRes ->
                with(authStore) {
                    accessToken = authRes.access_token
                    tokenType = authRes.token_type
                    expire = authRes.expire
                    refreshToken = authRes.refresh_token
                }
                _state.value = LoginState.Companies.Auth(authRes.toString())
            }.onFailure { throwable ->
                _state.value =
                    (throwable as? ClusterException)?.let { cluster ->
                        when (val typeMessage = cluster.error.message) {
                            is AuthErrorDto.Login -> LoginState.Error.Login(typeMessage.message)
                            is AuthErrorDto.Password -> LoginState.Error.Password(typeMessage.message)
                        }
                    } ?: LoginState.Error.Unknown("Неизвестная ошибка. Обратитесь к разработчику")
            }
        }
    }
}