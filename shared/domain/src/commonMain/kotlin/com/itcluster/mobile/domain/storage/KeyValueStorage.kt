package com.itcluster.mobile.domain.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.nullableInt
import com.russhwolf.settings.nullableString

class KeyValueStorage(settings: Settings) {
    var token by settings.nullableString("pref_token")
    var language by settings.nullableString("pref_language")

    var accessToken by settings.nullableString("access_token")
    var tokenType by settings.nullableString("token_type")
    var expireToken by settings.nullableInt("expire_token")
    var refreshToken by settings.nullableString("refresh_token")
}
