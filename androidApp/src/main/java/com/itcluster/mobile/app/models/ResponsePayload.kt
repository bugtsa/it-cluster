package com.itcluster.mobile.app.models

sealed class ResponsePayload {

    class IncomingText(
        val message: IncomingMessage
    ) : ResponsePayload() {

        override fun toString(): String {
            return "user: ${message.from} send message: ${message.text} in time: ${message.time}"
        }
    }

    class IncomingString(
        val message: String
    ) : ResponsePayload() {
        override fun toString(): String {
            return message
        }
    }
}