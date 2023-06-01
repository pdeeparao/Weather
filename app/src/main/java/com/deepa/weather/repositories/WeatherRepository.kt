package com.deepa.weather.repositories

import com.deepa.weather.data.network.Resource
import com.deepa.weather.models.Coord
import com.deepa.weather.models.GeoLocation
import com.deepa.weather.models.WeatherData

interface WeatherRepository {
    suspend fun getCityGeoCoordinates(name: String): Resource<List<GeoLocation>>
    suspend fun addCityToRecentList(geoLocation: GeoLocation)
    suspend fun getWeather(coord: Coord, isCurrentLocation:Boolean = false): Resource<WeatherData>
    suspend fun getWeatherForLastSearched(): Resource<WeatherData>?
    suspend fun getAllWeather(): List<Resource<WeatherData>>
    // List of past N search locations
    suspend fun getTrackedLocations(): List<Coord>
}