package com.deepa.weather.models

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("ih")
    val hourly: Double)