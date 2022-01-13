package com.itcluster.mobile.domain.repository

import dev.icerock.moko.network.generated.apis.NewsApi
import com.itcluster.mobile.domain.entity.News
import com.itcluster.mobile.domain.entity.toDomain
import com.itcluster.mobile.domain.storage.KeyValueStorage

class NewsRepository internal constructor(
    private val newsApi: NewsApi,
    private val keyValueStorage: KeyValueStorage
) {
    suspend fun getNewsList(query: String? = null, page: Int = 1, pageSize: Int = 20): List<News> {
        return newsApi.topHeadlinesGet(
            country = keyValueStorage.language,
            category = null,
            q = query,
            page = page,
            pageSize = pageSize
        ).articles.map { it.toDomain() }
    }
}
