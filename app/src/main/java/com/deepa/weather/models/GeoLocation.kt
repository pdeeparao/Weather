package com.deepa.weather.models

import com.google.gson.annotations.SerializedName

data class GeoLocation(
    @SerializedName("name") val cityName: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String
)