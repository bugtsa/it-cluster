package com.itcluster.mobile.app.models

import com.fasterxml.jackson.annotation.JsonProperty

data class OutGoingMessage(
    @field:JsonProperty("from")
    val from: String? = null,
    @field:JsonProperty("text")
    val text: String? = null
)