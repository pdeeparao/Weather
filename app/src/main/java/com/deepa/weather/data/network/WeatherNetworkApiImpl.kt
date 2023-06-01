package com.deepa.weather.data.network

import com.deepa.weather.data.WeatherNetworkApi
import com.deepa.weather.models.CurrentWeather
import com.deepa.weather.models.GeoLocation
import com.deepa.weather.network.WeatherApiService
import retrofit2.Response
import javax.inject.Inject

class WeatherNetworkApiImpl @Inject constructor(private val weatherApiService: WeatherApiService):
    WeatherNetworkApi {
    override suspend fun getGeoLocation(name: String): Response<List<GeoLocation>> {
        return weatherApiService.getGeoLocation(name)
    }
    override suspend fun getWeather(lat: Double, lon: Double): Response<CurrentWeather> {
        return weatherApiService.getWeatherForecast(lat, lon)
    }


}