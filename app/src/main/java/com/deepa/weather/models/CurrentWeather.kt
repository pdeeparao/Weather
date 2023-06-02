package com.deepa.weather.models

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    val coord: Coord,
    val weather: List<Weather>,

    @SerializedName("dt")
    val time: Long,
    @SerializedName("name")
    val cityName: String,
    @SerializedName("id")
    val cityId: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain,
    val clouds: Clouds
) {
    companion object {
        fun getCurrentWeather(coord: Coord): CurrentWeather {
            return CurrentWeather(
                coord = coord, cityName = "",
                weather = listOf(),
                main = Main(0.0, 0.0, 0.0, 0, 0),
                time = 0L, cityId = "", visibility = 0, wind = Wind(0.0), rain = Rain(0.0),
                clouds = Clouds(0)
            )

        }
    }
}
