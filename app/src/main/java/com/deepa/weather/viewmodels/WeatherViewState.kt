package com.deepa.weather.viewmodels

import com.deepa.weather.data.network.Resource
import com.deepa.weather.models.WeatherData

data class WeatherViewStateData(
    val viewMode: WeatherViewMode =WeatherViewMode.Initial,
    val data: List<Resource<WeatherData>>,
    val locationPermissionRequested: Boolean = false,
    val locationPermissionGranted: Boolean = false,
)


