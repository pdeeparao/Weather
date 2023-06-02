package com.deepa.weather.di

import android.content.Context
import com.deepa.weather.data.WeatherNetworkApi
import com.deepa.weather.data.network.WeatherNetworkApiImpl
import com.deepa.weather.network.BASE_URL
import com.deepa.weather.network.CACHE_SIZE
import com.deepa.weather.network.RETRO_API_KEY
import com.deepa.weather.network.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideWeatherNetworkApi(weatherNetworkApi: WeatherNetworkApiImpl): WeatherNetworkApi =
        weatherNetworkApi

    @Provides
    fun provideBaseUrl(): String = BASE_URL

    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache =
        Cache(context.cacheDir, CACHE_SIZE)

    @Singleton
    @Provides
    fun provideOkHttpClient(cache: Cache): OkHttpClient =
        OkHttpClient
            .Builder()
            .cache(cache)
            .addInterceptor {
                val request = it.request()
                val originalUrl = request.url()
                val httpUrl = originalUrl.newBuilder()
                    .addQueryParameter("appId", RETRO_API_KEY)
                    .build()
                val requestBuilder = request.newBuilder().url(httpUrl)
                it.proceed(requestBuilder.build())
            }
            .addInterceptor {
                val request = it.request()
                val cacheControl = CacheControl.Builder()
                    .maxAge(5, TimeUnit.MINUTES) // 5 minutes cache
                    .build()
                request.newBuilder().header("Cache-Control", cacheControl.toString()).build()
                it.proceed(request)
            }
            .build()


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(WeatherApiService::class.java)
}