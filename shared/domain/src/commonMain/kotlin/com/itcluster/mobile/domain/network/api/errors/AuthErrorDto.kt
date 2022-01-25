package com.itcluster.mobile.domain.network.api.errors

sealed class AuthErrorDto {

    class Login(
        val message: String
    ) : AuthErrorDto()

    class Password(
        val message: String
    ) : AuthErrorDto()
}