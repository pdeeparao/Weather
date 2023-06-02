package com.deepa.weather.data.local

import com.deepa.weather.data.CurrentLocationDataSource
import com.deepa.weather.data.location.WeatherLocationManager
import com.deepa.weather.models.Coord
import javax.inject.Inject

class CurrentLocationDataSourceImpl @Inject constructor(
    private val weatherLocationManager: WeatherLocationManager
) : CurrentLocationDataSource {
    override fun getCurrentLocation(): Coord? {
        return weatherLocationManager.getCurrentLocation()
    }
}