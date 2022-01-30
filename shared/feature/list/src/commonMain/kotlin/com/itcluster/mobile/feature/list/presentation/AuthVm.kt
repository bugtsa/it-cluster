package com.itcluster.mobile.feature.list.presentation

import com.itcluster.mobile.domain.network.ItClusterSDK
import com.itcluster.mobile.domain.network.api.errors.AuthErrorDto
import com.itcluster.mobile.domain.network.api.errors.ClusterException
import com.itcluster.mobile.domain.network.models.auth.LoginReq
import com.itcluster.mobile.feature.list.model.CompaniesStore
import com.itcluster.mobile.feature.list.model.state.LoginState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AuthVm(
    private val companiesStore: CompaniesStore
) : ViewModel() {

    private val sdk: ItClusterSDK by lazy { ItClusterSDK() }

    private val mainScope = MainScope()

    val auth: LiveData<LoginState> get() = _authState

    private val _authState: MutableLiveData<LoginState> = MutableLiveData(LoginState.NoState)

    fun companiesListRequest(login: String, password: String) {
        val req = LoginReq(
            login,
            password
        )
        mainScope.launch {
            kotlin.runCatching {
                sdk.companiesRequest(req)
            }.onSuccess { companies ->
                companiesStore.login = login
                companiesStore.password = password
                companiesStore.companies = companies.toMutableList()
                _authState.value = LoginState.LoginFirst.Companies
            }.onFailure { throwable ->
                _authState.value =
                    (throwable as? ClusterException)?.let { cluster ->
                        when (val typeMessage = cluster.error.message) {
                            is AuthErrorDto.Login -> LoginState.Error.Login(typeMessage.message)
                            is AuthErrorDto.Password -> LoginState.Error.Password(typeMessage.message)
                        }
                    } ?: LoginState.Error.Unknown(
                        "Неизвестная ошибка. Обратитесь к разработчику",
                        throwable
                    )
            }
        }
    }
}