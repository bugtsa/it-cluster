package com.itcluster.mobile.kmm.shared.cache

import com.itcluster.mobile.MR
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.Napier
import com.russhwolf.settings.Settings
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.units.TableUnitItem
import io.ktor.client.engine.HttpClientEngine
import com.itcluster.mobile.domain.di.DomainFactory
import com.itcluster.mobile.domain.entity.News
import com.itcluster.mobile.feature.config.di.ConfigFactory
import com.itcluster.mobile.feature.config.model.ConfigStore
import com.itcluster.mobile.feature.config.presentation.ConfigViewModel
import com.itcluster.mobile.feature.list.di.AuthFactory
import com.itcluster.mobile.feature.list.di.ListFactory
import com.itcluster.mobile.feature.list.di.MainPageFactory
import com.itcluster.mobile.feature.list.model.AuthStore
import com.itcluster.mobile.feature.list.model.ListSource
import com.itcluster.mobile.feature.list.presentation.ListViewModel
import dev.icerock.moko.resources.desc.desc

class SharedFactory(
    settings: Settings,
    antilog: Antilog,
    baseUrl: String,
    newsUnitsFactory: NewsUnitsFactory,
    httpClientEngine: HttpClientEngine?
) {
    // special for iOS call side we not use argument with default value
    constructor(
        settings: Settings,
        antilog: Antilog,
        baseUrl: String,
        newsUnitsFactory: NewsUnitsFactory
    ) : this(
        settings = settings,
        antilog = antilog,
        baseUrl = baseUrl,
        newsUnitsFactory = newsUnitsFactory,
        httpClientEngine = null
    )

    private val domainFactory = DomainFactory(
        settings = settings,
        baseUrl = baseUrl,
        httpClientEngine = httpClientEngine
    )

    val authStore = object : AuthStore {
        override var accessToken: String?
            get() = domainFactory.authRepository.accessToken
            set(value) {
                domainFactory.authRepository.accessToken = value
            }

        override var tokenType: String?
            get() = domainFactory.authRepository.tokenType
            set(value) {
                domainFactory.authRepository.tokenType = value
            }

        override var expire: Int?
            get() = domainFactory.authRepository.expireToken
            set(value) {
                domainFactory.authRepository.expireToken = value
            }

        override var refreshToken: String?
            get() = domainFactory.authRepository.refreshToken
            set(value) {
                domainFactory.authRepository.refreshToken = value
            }
    }

    val authFactory = AuthFactory(
        authStore = authStore
    )

    val mainPageFactory: MainPageFactory = MainPageFactory(
        authStore = authStore
    )

    val newsFactory: ListFactory<News> = ListFactory(
        listSource = object : ListSource<News> {
            override suspend fun getList(): List<News> {
                return domainFactory.newsRepository.getNewsList()
            }
        },
        strings = object : ListViewModel.Strings {
            override val unknownError: StringResource = MR.strings.unknown_error
        },
        unitsFactory = object : ListViewModel.UnitsFactory<News> {
            override fun createTile(data: News): TableUnitItem {
                return newsUnitsFactory.createNewsTile(
                    id = data.id.toLong(),
                    title = data.fullName.orEmpty(),
                    description = data.description?.desc() ?: MR.strings.no_description.desc()
                )
            }
        }
    )

    val configFactory = ConfigFactory(
        configStore = object : ConfigStore {
            override var apiToken: String?
                get() = domainFactory.configRepository.apiToken
                set(value) {
                    domainFactory.configRepository.apiToken = value
                }
            override var language: String?
                get() = domainFactory.configRepository.language
                set(value) {
                    domainFactory.configRepository.language = value
                }
        },
        validations = object : ConfigViewModel.Validations {
            override fun validateToken(value: String): StringDesc? {
                return if (value.isBlank()) {
                    MR.strings.invalid_token.desc()
                } else {
                    null
                }
            }

            override fun validateLanguage(value: String): StringDesc? {
                val validValues = listOf("ru", "us")
                return if (validValues.contains(value)) {
                    null
                } else {
                    StringDesc.ResourceFormatted(
                        MR.strings.invalid_language_s,
                        validValues.joinToString(", ")
                    )
                }
            }
        },
        defaultToken = "ed155d0a445e4b4fbd878fe1f3bc1b7f",
        defaultLanguage = "us"
    )

    init {
        Napier.base(antilog)
    }

    interface NewsUnitsFactory {
        fun createNewsTile(
            id: Long,
            title: String,
            description: StringDesc
        ): TableUnitItem
    }
}
