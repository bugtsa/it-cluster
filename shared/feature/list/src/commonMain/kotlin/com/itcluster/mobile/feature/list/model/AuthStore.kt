package com.itcluster.mobile.feature.list.model

interface AuthStore {

    var accessToken: String?
    var tokenType: String?
    var expire: Int?
    var refreshToken: String?
}