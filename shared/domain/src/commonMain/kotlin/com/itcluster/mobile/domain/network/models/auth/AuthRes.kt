package com.itcluster.mobile.domain.network.models.auth

import kotlinx.serialization.Serializable

@Serializable
class AuthRes(
    val access_token: String,
    val token_type: String,
    val expire: Int,
    val refresh_token: String
) {
    override fun toString(): String {
        return "your access token: $access_token and type: $token_type"
    }
}