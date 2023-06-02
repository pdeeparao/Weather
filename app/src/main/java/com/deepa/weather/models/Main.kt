package com.deepa.weather.models

import com.google.gson.annotations.SerializedName

data class Main(
    val temp: Double,
    @SerializedName("temp_min")
    val low: Double,
    @SerializedName("temp_max")
    val high: Double,
    val humidity: Int,
    val pressure: Int
)