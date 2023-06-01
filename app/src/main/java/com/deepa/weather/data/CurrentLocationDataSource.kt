package com.deepa.weather.data

import com.deepa.weather.models.Coord

interface CurrentLocationDataSource {
    fun getCurrentLocation(): Coord?
}