package com.deepa.weather.data

import com.deepa.weather.models.Coord

interface SearchDatasource {
    suspend fun getRecentSearchLocations(): List<Coord>
    suspend fun saveSearchLocation(geoLocation: Coord)
    suspend fun getLastSearchLocation(): Coord?
}