package com.itcluster.mobile.feature.list.model.state

import com.itcluster.mobile.presentation.models.CompanyModel

sealed class LoginState {

    object NoState: LoginState()

    sealed class Error : LoginState() {
        class Login(
            val message: String
        ) : Error()

        class Password(
            val message: String
        ) : Error()

        class Unknown(
            val message: String,
            val throwable: Throwable
        ) : Error()
    }

    sealed class Authorized : LoginState() {

        object UnAuthorized: LoginState()

        object SuccessAuthorized: LoginState()
    }

    sealed class LoginFirst : LoginState() {

        object Companies: LoginFirst()
    }

    sealed class Companies: LoginState() {

        class Data(
            val companies: List<CompanyModel>
        ) : Companies()

        class Auth(
            val token: String
        ) : Companies()
    }


}
