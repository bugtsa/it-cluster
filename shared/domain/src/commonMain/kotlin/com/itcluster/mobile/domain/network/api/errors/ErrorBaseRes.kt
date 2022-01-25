package com.itcluster.mobile.domain.network.api.errors

import kotlinx.serialization.Serializable

@Serializable
data class ErrorBaseRes(

    val code: Int? = null,

    val name: String? = null,

    val message: String? = null,

    val status: Int? = null
)
