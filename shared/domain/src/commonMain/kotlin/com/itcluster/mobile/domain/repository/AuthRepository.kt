package com.itcluster.mobile.domain.repository

import com.itcluster.mobile.domain.storage.KeyValueStorage

class AuthRepository(private val keyValueStorage: KeyValueStorage) {

    var accessToken: String?
        get() = keyValueStorage.accessToken
        set(value) {
            keyValueStorage.accessToken = value
        }

    var tokenType: String?
        get() = keyValueStorage.tokenType
        set(value) {
            keyValueStorage.tokenType = value
        }

    var expireToken: Int?
        get() = keyValueStorage.expireToken
        set(value) {
            keyValueStorage.expireToken = value
        }

    var refreshToken: String?
        get() = keyValueStorage.refreshToken
        set(value) {
            keyValueStorage.refreshToken = value
        }
}