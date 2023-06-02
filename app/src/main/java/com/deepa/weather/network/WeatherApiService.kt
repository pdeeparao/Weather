package com.deepa.weather.network

import com.deepa.weather.models.CurrentWeather
import com.deepa.weather.models.GeoLocation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.openweathermap.org"
const val IMAGE_URL = "http://openweathermap.org/img/w/%s.png"
const val RETRO_API_KEY = "d4f54a799f459b2f859d2745f6fc2753"
const val CACHE_SIZE = (5 * 1024 * 1024).toLong()

interface WeatherApiService {
    @GET("geo/1.0/direct")
    suspend fun getGeoLocation(@Query("q") name: String): Response<List<GeoLocation>>

    @GET("data/2.5/weather")
    suspend fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String = "imperial"
    ): Response<CurrentWeather>

}