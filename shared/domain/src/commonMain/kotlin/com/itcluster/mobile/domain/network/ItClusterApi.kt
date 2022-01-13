package com.itcluster.mobile.domain.network

import com.itcluster.mobile.domain.network.models.auth.AuthRes
import com.itcluster.mobile.domain.network.models.auth.LoginReq
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class ItClusterApi {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            }
            serializer = KotlinxSerializer(json)
        }
    }

    suspend fun postAuth(req: LoginReq): AuthRes = httpClient.post {
        url("$IT_CLUSTER_ENDPOINT$AUTH_LOGIN")
        body = FormDataContent(Parameters.build {
            append("login", req.login)
            append("password" , req.password)
            append( "company_id", req.company_id)
        })
    }

    companion object {
        private const val IT_CLUSTER_ENDPOINT = "https://api.qualitylive.su/v1/"

        private const val AUTH_LOGIN = "auth/login"
    }
}

