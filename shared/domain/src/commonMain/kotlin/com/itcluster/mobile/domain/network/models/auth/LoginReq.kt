package com.itcluster.mobile.domain.network.models.auth

import com.itcluster.mobile.domain.Constants.Companion.EMPTY_SEPARATOR
import kotlinx.serialization.Serializable

@Serializable
class LoginReq(
    val login: String,
    val password: String,
    val company_id: String = EMPTY_SEPARATOR
)