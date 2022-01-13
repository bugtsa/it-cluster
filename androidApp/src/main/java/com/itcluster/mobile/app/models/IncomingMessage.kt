package com.itcluster.mobile.app.models

import com.fasterxml.jackson.annotation.JsonProperty

class IncomingMessage(
    @field:JsonProperty("from")
    val from: String? = "",
    @field:JsonProperty("text")
    val text: String? = "",
    @field:JsonProperty("time")
    val time: String? = ""
)