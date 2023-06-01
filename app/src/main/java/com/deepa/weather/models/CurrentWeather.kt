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
)
