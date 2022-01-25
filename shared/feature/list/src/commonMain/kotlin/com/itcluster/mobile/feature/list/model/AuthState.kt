package com.itcluster.mobile.feature.list.model

sealed class AuthState {

    class Success(
        val token: String
    ) : AuthState()

    sealed class Error : AuthState() {
        class Login(
            val message: String
        ) : Error()

        class Password(
            val message: String
        ) : Error()

        class Unknown(
            val message: String
        ) : Error()
    }

    object NoAction : AuthState()
}
