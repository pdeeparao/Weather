package com.deepa.weather.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepa.weather.data.local.SharedPreferenceHelper
import com.deepa.weather.data.location.WeatherLocationManager
import com.deepa.weather.data.network.Resource
import com.deepa.weather.data.network.Status
import com.deepa.weather.repositories.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to show the main weather screen.
 * It keeps track of the current view state by keeping track of user inputs like which location is being viewed, new search request
 * Gets the needed data for UI from repository
 * Keeps the data up to date based on the user events and ui state change
 * Maintains view state that can be consumed by View layer.
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherLocationManager: WeatherLocationManager,
    private val weatherRepository: WeatherRepository,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : ViewModel() {
    private val viewStateData: MutableLiveData<WeatherViewStateData> =
        MutableLiveData(
            WeatherViewStateData(
                viewMode = WeatherViewMode.Detail(null),
                listOf(),
                sharedPreferenceHelper.isLocationPermissionDialogShown(),
                weatherLocationManager.isLocationPermissionGranted()
            )
        )
    val viewState: LiveData<WeatherViewStateData> = viewStateData
    private val searchLocationStatusData: MutableLiveData<Resource<Boolean>> =
        MutableLiveData(Resource.success(true))
    val searchLocationStatus: LiveData<Resource<Boolean>> = searchLocationStatusData
    private val TAG: String = WeatherViewModel::class.java.canonicalName

    fun onPermissionDialogShown() {
        sharedPreferenceHelper.onLocationPermissionDialogShown()
    }

    fun onPermissionGranted() {
        sharedPreferenceHelper.onLocationPermissionDialogShown()
        val currState = viewStateData.value ?: return
        viewStateData.value =
            currState.copy(locationPermissionGranted = weatherLocationManager.isLocationPermissionGranted())
        viewModelScope.launch {
            refreshData("PermissionGranted")
        }
    }

    fun userSearchLocation() {
        val currState = viewStateData.value ?: return
        viewModelScope.launch {
            searchLocationStatusData.value = Resource.loading(true)
            val weatherData = weatherRepository.getWeatherForLastSearched()
            if (weatherData == null) {
                Log.e(TAG, "Last search location is null")
            }
            val newList = ArrayList(currState.data)
            newList.add(weatherData)

            if (weatherData == null || weatherData.status == Status.ERROR) {
                searchLocationStatusData.value =
                    Resource.error(
                        "Failed to load the weather. (Exception: ${weatherData?.message ?: "Unknown"})Try again",
                        false
                    )
            } else {
                val nextState = currState.copy(
                    data = newList,
                    viewMode = WeatherViewMode.Detail(weatherData?.data?.coor)
                )
                changeState(currState, nextState, "UserSearchLocation: ")
            }
        }
    }

    fun updateCurrentDetail(pos: Int) {
        val currState = viewStateData.value ?: return
        if (pos < currState.data.size) {
            currState.data[pos].data?.coor?.let {
                val nextState = currState.copy(viewMode = WeatherViewMode.Detail(it))
                changeState(currState, nextState, "updateCurrentDetail to position: $pos")
            }
        }
    }


    fun setupDefaultView() {
        val currState = viewStateData.value ?: return
        viewModelScope.launch {
            refreshData()
        }
    }

    fun onViewResumed() {
        viewModelScope.launch {
            refreshData("onResume")
        }

    }

    private suspend fun refreshData(context: String = "") {
        val listOfWeather = weatherRepository.getAllWeather()
        val currState = viewStateData.value ?: return
        val nextState = currState.copy(data = listOfWeather)
        changeState(currState, nextState, "refreshData for $context")
    }


    private fun changeState(
        prevState: WeatherViewStateData,
        nextState: WeatherViewStateData,
        input: String
    ) {
        viewStateData.value = nextState
        logEvent(prevState, nextState, input)

    }

    private fun logEvent(
        prevState: WeatherViewStateData,
        nextState: WeatherViewStateData,
        input: String
    ) {
        Log.d(TAG, "$prevState + $input -> $nextState.")
    }
}