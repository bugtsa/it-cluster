package com.itcluster.mobile.domain.network.models.auth

import kotlinx.serialization.Serializable

@Serializable
class LoginReq(
    val login: String,
    val password: String,
    val company_id: String
)