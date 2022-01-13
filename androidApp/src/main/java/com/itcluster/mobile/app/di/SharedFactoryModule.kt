package com.itcluster.mobile.app.di

import com.russhwolf.settings.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.aakira.napier.Antilog
import javax.inject.Singleton
import com.itcluster.mobile.app.BuildConfig
import com.itcluster.mobile.kmm.shared.cache.SharedFactory

/**
 * Module, that provides shared mutliplatform factory for Android platform
 */
@InstallIn(SingletonComponent::class)
@Module
object SharedFactoryModule {
    @Provides
    @Singleton
    fun provideMultiplatformFactory(
        settings: Settings,
        antilog: Antilog,
        newsUnitFactory: SharedFactory.NewsUnitsFactory
    ): SharedFactory = SharedFactory(
        settings = settings,
        antilog = antilog,
        newsUnitsFactory = newsUnitFactory,
        baseUrl = BuildConfig.BASE_URL
    )
}
