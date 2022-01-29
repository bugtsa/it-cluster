package com.itcluster.mobile.domain.network.models.auth

import com.itcluster.mobile.domain.Constants.Companion.LONG_DEFAULT
import kotlinx.serialization.Serializable

@Serializable
class LoginReq(
    val login: String,
    val password: String,
    val company_id: Long = LONG_DEFAULT
)