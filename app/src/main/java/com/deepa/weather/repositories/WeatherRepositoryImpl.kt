package com.deepa.weather.repositories

import com.deepa.weather.data.CurrentLocationDataSource
import com.deepa.weather.data.SearchDatasource
import com.deepa.weather.data.network.Resource
import com.deepa.weather.data.WeatherNetworkApi
import com.deepa.weather.models.Coord
import com.deepa.weather.models.GeoLocation
import com.deepa.weather.models.WeatherData
import javax.inject.Inject

/**
 * Implementation for WeatherRepository.
 * 1. Caching and persistence - Makes use of Retrofit to provide caching. Retrofit caches the response for 10 mins. To keep up
 * with weather changes, responses are cached for 10 mins and after that, weather update is fetched
 * from server. More complicated caching can be provided if the data is persisted in database and validate if the data is still relevant based on current time.
 * 2. Has 2 data sources - Current location data source and saved search datasource.
 */
class WeatherRepositoryImpl @Inject constructor(
    private val weatherNetworkApi: WeatherNetworkApi,
    private val currentLocationDataSource: CurrentLocationDataSource,
    private val searchDatasource: SearchDatasource
) : WeatherRepository {
    override suspend fun getCityGeoCoordinates(name: String): Resource<List<GeoLocation>> {
        // Pick from network and/or cache and return result
        val response = weatherNetworkApi.getGeoLocation(name)
        return if (response.isSuccessful) {
            Resource.success(response.body())
        } else {
            Resource.error(response.errorBody().toString(), null)
        }
    }

    override suspend fun addCityToRecentList(geoLocation: GeoLocation) {
        searchDatasource.saveSearchLocation(Coord(geoLocation.lat, geoLocation.lon))
    }

    override suspend fun getWeather(
        coord: Coord,
        isCurrentLocation: Boolean
    ): Resource<WeatherData> {
        val response = weatherNetworkApi.getWeather(coord.lat, coord.lon)
        return if (response.isSuccessful) {
            val current = response.body()
            current?.let { Resource.success(WeatherData(coord, isCurrentLocation, it)) }
                ?: Resource.error("Unknown Error", null)
        } else {
            Resource.error(
                response.errorBody().toString(),
                WeatherData(coord, isCurrentLocation, null)
            )
        }
    }

    override suspend fun getWeatherForLastSearched(): Resource<WeatherData>? {
        val coord = searchDatasource.getLastSearchLocation() ?: return null
        return getWeather(coord)
    }

    override suspend fun getAllWeather(): List<Resource<WeatherData>> {
        //If location permission is granted, current location is always first. And the saved search results.
        val recentSearches = searchDatasource.getRecentSearchLocations()
        val currentLocation = currentLocationDataSource.getCurrentLocation()
        val result = ArrayList<Resource<WeatherData>>()
        if (currentLocation != null) {
            val weatherData = getWeather(currentLocation, true)
            result.add(weatherData)

        }
        recentSearches.forEach {
            val weatherData = getWeather(it)
            result.add(weatherData)
        }
        return result
    }

    override suspend fun getTrackedLocations(): List<Coord> {
        return searchDatasource.getRecentSearchLocations()
    }
}