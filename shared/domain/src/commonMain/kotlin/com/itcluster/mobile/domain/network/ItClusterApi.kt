package com.itcluster.mobile.domain.network

import com.itcluster.mobile.domain.network.api.errors.ClusterException
import com.itcluster.mobile.domain.network.api.errors.ErrorBaseRes
import com.itcluster.mobile.domain.network.api.errors.ErrorClusterDto.Companion.toDto
import com.itcluster.mobile.domain.network.api.errors.MessageErrorRes
import com.itcluster.mobile.domain.network.models.auth.AuthRes
import com.itcluster.mobile.domain.network.models.auth.CompaniesRes
import com.itcluster.mobile.domain.network.models.auth.LoginReq
import com.itcluster.mobile.domain.network.models.wallet.WalletRes
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

    suspend fun authToken(req: LoginReq): AuthRes = httpClient.post {
        url("$IT_CLUSTER_ENDPOINT$AUTH_TOKEN")
        body = FormDataContent(Parameters.build {
            append(LOGIN_PARAM, req.login)
            append(PASSWORD_PARAM, req.password)
            append("company_id", req.company_id.toString())
        })
    }

    suspend fun companiesList(req: LoginReq): CompaniesRes = httpClient.post {
        url("$IT_CLUSTER_ENDPOINT$COMPANIES_LIST")
        body = FormDataContent(Parameters.build {
            append(LOGIN_PARAM, req.login)
            append(PASSWORD_PARAM, req.password)
        })
    }

    suspend fun walletList(authToken: String): List<WalletRes> = httpClient.get {
        url("$IT_CLUSTER_ENDPOINT$WALLET_LIST")
        header(AUTHORIZATION_HEADER, "$BEARER_PREFIX $authToken")
    }

    companion object {
        private const val IT_CLUSTER_ENDPOINT = "https://api.qualitylive.su/v1/"

        private const val COMPANIES_LIST = "auth/login-first"
        private const val AUTH_TOKEN = "auth/login"

        private const val WALLET_LIST = "wallet/list"
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer"

        private const val LOGIN_PARAM = "login"
        private const val PASSWORD_PARAM = "password"
    }
}

