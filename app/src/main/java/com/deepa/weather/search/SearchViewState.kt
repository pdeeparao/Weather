package com.deepa.weather.search

import com.deepa.weather.models.GeoLocation
import java.lang.Exception


data class SearchViewState(val searchRequest: SearchRequest, val searchState: SearchState)

data class SearchRequest(val cityOrZipCode: String="", val countryCode: String="")

sealed class SearchState{
    object Initial: SearchState()
    class Searching(val searchRequest: SearchRequest): SearchState()
    class SearchResult(val results: List<GeoLocation>): SearchState()
    class Error(val error: SearchErrorState): SearchState()
}
sealed class SearchErrorState{
    class InvalidCityOrZipCode(val cityName: String): SearchErrorState()
    object MissingCityOrZipCode: SearchErrorState()
    class Error(val errorMessage: String, exception: Exception): SearchErrorState()
}