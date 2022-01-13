package com.itcluster.mobile.app.di

import com.itcluster.mobile.app.units.NewsListUnitsFactory
import com.itcluster.mobile.kmm.shared.cache.SharedFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class UnitFactoryModule {
    @Binds
    abstract fun bindNewsListUnitFactory(impl: NewsListUnitsFactory): SharedFactory.NewsUnitsFactory
}
