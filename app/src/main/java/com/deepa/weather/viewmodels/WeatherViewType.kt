package com.deepa.weather.viewmodels

enum class WeatherViewType(val value: Int) {
    Default(1),
    Detail(2),
    UserSearch(3),
    Summary(4),
    CurrentLocation(5);

    companion object {
        fun getWeatherViewType(value: Int?): WeatherViewType {
            return when (value) {
                1 -> Default
                2 -> Detail
                3 -> UserSearch
                4 -> Summary
                5-> CurrentLocation
                else -> Default
            }
        }
    }
}