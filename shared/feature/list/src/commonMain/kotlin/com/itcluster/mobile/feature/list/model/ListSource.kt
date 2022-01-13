package com.itcluster.mobile.feature.list.model

interface ListSource<T> {
    suspend fun getList(): List<T>
}
