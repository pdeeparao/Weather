package com.deepa.weather.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepa.weather.data.network.Resource
import com.deepa.weather.data.network.Status
import com.deepa.weather.models.GeoLocation
import com.deepa.weather.repositories.WeatherRepository
import com.deepa.weather.search.SearchErrorState
import com.deepa.weather.search.SearchRequest
import com.deepa.weather.search.SearchState
import com.deepa.weather.search.SearchViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel(){
    private val viewStateData: MutableLiveData<SearchViewState> =
        MutableLiveData(SearchViewState(SearchRequest(), SearchState.Initial))
    val viewState :LiveData<SearchViewState> = viewStateData
    private val TAG: String = SearchViewModel::class.java.canonicalName

    fun onSearchStarted(cityOrZipCode: String, countryCode: String){
        val currState = viewState.value?:run {
            Log.e(TAG, "viewState not set")
            return
        }

        if(currState.searchState is SearchState.Searching){
            Log.e(TAG, "Search is ongoing")
            return
        }

        val input = SearchRequest(cityOrZipCode, countryCode)
        if(cityOrZipCode.isBlank()) {
            val error = SearchState.Error(SearchErrorState.MissingCityOrZipCode)
            viewStateData.value = currState.copy(searchRequest = input, searchState= error)
        }
        viewStateData.value = currState.copy(searchRequest = input, searchState = SearchState.Searching(
            input
        )
        )
        val queryName = if(countryCode.isBlank()) cityOrZipCode else StringBuffer(cityOrZipCode).append(",").append(countryCode).toString()
        viewModelScope.launch {
            try {
                val result: Resource<List<GeoLocation>> =
                    weatherRepository.getCityGeoCoordinates(queryName)
                val nextState = if (result.status == Status.SUCCESS) {
                    val geoLocation = result.data ?: listOf()
                    if (geoLocation.size == 1) {
                        weatherRepository.addCityToRecentList(geoLocation[0])
                        currState.copy(
                            searchState = SearchState.SearchResult(
                                result.data ?: listOf()
                            )
                        )
                    } else {
                        currState.copy(
                            searchState = SearchState.SearchResult(
                                result.data ?: listOf()
                            )
                        )
                    }

                } else {
                    val error: SearchErrorState =
                        SearchErrorState.Error(result.message ?: "Error", Exception(result.message))
                    currState.copy(searchState = SearchState.Error(error))
                }
                viewStateData.value = nextState
            }catch (exception: Exception){
                viewStateData.value = currState.copy(searchState = SearchState.Error(SearchErrorState.Error(exception.message?:"Error getting search location. Try again", exception)))
            }
        }
    }
}