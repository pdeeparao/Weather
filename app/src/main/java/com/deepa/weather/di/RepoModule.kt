package com.deepa.weather.di

import com.deepa.weather.data.CurrentLocationDataSource
import com.deepa.weather.data.SearchDatasource
import com.deepa.weather.data.local.CurrentLocationDataSourceImpl
import com.deepa.weather.data.local.SearchDatasourceImpl
import com.deepa.weather.repositories.WeatherRepository
import com.deepa.weather.repositories.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun provideSearchRepository(searchRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    abstract fun provideSearchDataSource(searchDatasourceImpl: SearchDatasourceImpl): SearchDatasource

    @Binds
    abstract fun provideCurrentLocationDataSourceImpl(currentLocationDataSource: CurrentLocationDataSourceImpl): CurrentLocationDataSource
}