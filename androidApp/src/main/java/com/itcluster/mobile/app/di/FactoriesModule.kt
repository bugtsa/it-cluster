package com.itcluster.mobile.app.di

import com.itcluster.mobile.domain.entity.News
import com.itcluster.mobile.feature.config.di.ConfigFactory
import com.itcluster.mobile.feature.list.di.*
import com.itcluster.mobile.kmm.shared.cache.SharedFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object FactoriesModule {
    @Provides
    fun provideNewsFactory(sharedFactory: SharedFactory): ListFactory<News> =
        sharedFactory.newsFactory

    @Provides
    fun provideConfigFactory(sharedFactory: SharedFactory): ConfigFactory =
        sharedFactory.configFactory

    @Provides
    fun provideAuthFactory(sharedFactory: SharedFactory): AuthFactory =
        sharedFactory.authFactory

    @Provides
    fun provideCompaniesFactory(sharedFactory: SharedFactory): CompaniesFactory =
        sharedFactory.companiesFactory

    @Provides
    fun provideWalletsFactory(sharedFactory: SharedFactory): WalletsFactory =
        sharedFactory.mainPageFactory

    @Provides
    fun provideWalletDetailFactory(sharedFactory: SharedFactory): WalletDetailFactory =
        sharedFactory.transactionFactory
}
