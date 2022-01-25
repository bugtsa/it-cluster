package com.itcluster.mobile.domain.network.api.errors

import kotlinx.serialization.Serializable

@Serializable
class MessageErrorRes(
    val fields: List<String>?,

    val errors: Map<String, List<String>>?
) {

    companion object {

        fun empty() : MessageErrorRes =
            MessageErrorRes(
                emptyList(),
                emptyMap()
            )
    }
}