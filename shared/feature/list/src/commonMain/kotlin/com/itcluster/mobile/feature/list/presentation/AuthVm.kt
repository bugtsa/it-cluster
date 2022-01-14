package com.itcluster.mobile.feature.list.presentation

import com.itcluster.mobile.domain.network.ItClusterSDK
import com.itcluster.mobile.domain.network.models.auth.LoginReq
import com.itcluster.mobile.feature.list.model.AuthStore
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AuthVm(
    private val authStore: AuthStore
) : ViewModel() {

    private val sdk: ItClusterSDK by lazy { ItClusterSDK() }

    private val mainScope = MainScope()

    val auth: LiveData<String> get() = _authString

    private val _authString: MutableLiveData<String> = MutableLiveData("")

    fun onCreated() {}

    fun requestAuth(login: String, password: String) {
        val req = LoginReq(
            login,
            password,
            "96"
        )
        mainScope.launch {
            kotlin.runCatching {
                sdk.requestAuth(req)
            }.onSuccess { authRes ->
                with(authStore) {
                    accessToken = authRes.access_token
                    tokenType = authRes.token_type
                    expire = authRes.expire
                    refreshToken = authRes.refresh_token
                }
                _authString.value = authRes.toString()
            }.onFailure {
                _authString.value = it.toString()
            }
        }
    }
}