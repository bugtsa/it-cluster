package com.itcluster.mobile.domain.network

import com.itcluster.mobile.domain.network.api.errors.ClusterException
import com.itcluster.mobile.domain.network.api.errors.ErrorBaseRes
import com.itcluster.mobile.domain.network.api.errors.ErrorClusterDto.Companion.toDto
import com.itcluster.mobile.domain.network.api.errors.MessageErrorRes
import com.itcluster.mobile.domain.network.models.auth.AuthRes
import com.itcluster.mobile.domain.network.models.auth.LoginReq
import io.ktor.client.HttpClient
import io.ktor.client.features.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class ItClusterApi {

    private val json = Json {
        ignoreUnknownKeys = true
        allowStructuredMapKeys = true
        useAlternativeNames = false
    }

    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)

        }
        HttpResponseValidator {
            handleResponseException { exception ->
                val clientException =
                    exception as? ClientRequestException ?: return@handleResponseException
                val baseRes = Json.decodeFromString(
                    ErrorBaseRes.serializer(),
                    clientException.response.readText()
                )

                val message = baseRes.message
                    ?.let {
                        json.decodeFromString(MessageErrorRes.serializer(), it)
                    } ?: MessageErrorRes.empty()
                throw ClusterException(
                    baseRes.toDto(message)
                )
            }
        }
    }

    suspend fun postAuth(req: LoginReq): AuthRes = httpClient.post {
        url("$IT_CLUSTER_ENDPOINT$AUTH_LOGIN")
        body = FormDataContent(Parameters.build {
            append("login", req.login)
            append("password", req.password)
            append("company_id", req.company_id)
        })
    }

    companion object {
        private const val IT_CLUSTER_ENDPOINT = "https://api.qualitylive.su/v1/"

        private const val AUTH_LOGIN = "auth/login"
    }
}

