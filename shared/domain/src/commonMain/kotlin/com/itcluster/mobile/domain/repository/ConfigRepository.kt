package com.itcluster.mobile.domain.repository

import com.itcluster.mobile.domain.storage.KeyValueStorage

class ConfigRepository(
    private val keyValueStorage: KeyValueStorage
) {
    var apiToken: String?
        get() = keyValueStorage.token
        set(value) {
            keyValueStorage.token = value
        }

    var language: String?
        get() = keyValueStorage.language
        set(value) {
            keyValueStorage.language = value
        }
}
