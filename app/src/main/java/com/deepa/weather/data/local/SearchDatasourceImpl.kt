package com.deepa.weather.data.local

import com.deepa.weather.data.SearchDatasource
import com.deepa.weather.models.Coord
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.inject.Inject


/**
 * SearchDatasource implementation uses shared preferences to store only one search location.
 * This can be replaced with MySQL database to allow user to search and as many locations as possible.
 */
class SearchDatasourceImpl @Inject constructor(private val sharedPreferenceHelper: SharedPreferenceHelper) :
    SearchDatasource {
    companion object {
        val recentSearchLocation: String = "recent_locations"
    }

    override suspend fun saveSearchLocation(coord: Coord) {
        sharedPreferenceHelper.saveData(recentSearchLocation, coord)
    }

    override suspend fun getRecentSearchLocations(): List<Coord> {
        // Currently only last search is returned. The logic can be modified to return last x locations.
        val type: Type = object : TypeToken<Coord?>() {}.type
        val coord: Coord? = sharedPreferenceHelper.getData(recentSearchLocation, type)
        return coord?.let { arrayListOf(coord) } ?: listOf()
    }

    override suspend fun getLastSearchLocation(): Coord? {
        val locations = getRecentSearchLocations()
        return if (locations.isNotEmpty()) locations[0] else null
    }
}