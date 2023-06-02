package com.deepa.weather.viewmodels

import com.deepa.weather.models.Coord

sealed class WeatherViewMode {
    object Initial : WeatherViewMode()
    object Loading : WeatherViewMode()
    object Summary : WeatherViewMode()
    class Detail(val coord: Coord? = null) : WeatherViewMode()
    object CurrentLocation : WeatherViewMode()
}