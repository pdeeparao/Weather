package com.deepa.weather.data

import com.deepa.weather.models.CurrentWeather
import com.deepa.weather.models.GeoLocation
import retrofit2.Response

interface WeatherNetworkApi {
    suspend fun getGeoLocation(name: String): Response<List<GeoLocation>>
    suspend fun getWeather(lat: Double, lon: Double): Response<CurrentWeather>
}