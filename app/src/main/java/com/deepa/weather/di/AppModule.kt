package com.deepa.weather.di

import android.content.Context
import com.deepa.weather.data.local.SharedPreferenceHelper
import com.deepa.weather.data.location.WeatherLocationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferenceHelper(@ApplicationContext context: Context): SharedPreferenceHelper =
        SharedPreferenceHelper(context)

    @Singleton
    @Provides
    fun provideLocationManager(@ApplicationContext context: Context): WeatherLocationManager =
        WeatherLocationManager(context)
}