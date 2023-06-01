package com.deepa.weather

import android.os.Looper
import com.deepa.weather.data.local.SharedPreferenceHelper
import com.deepa.weather.data.location.WeatherLocationManager
import com.deepa.weather.data.network.Resource
import com.deepa.weather.models.Clouds
import com.deepa.weather.models.Coord
import com.deepa.weather.models.CurrentWeather
import com.deepa.weather.models.Main
import com.deepa.weather.models.Rain
import com.deepa.weather.models.WeatherData
import com.deepa.weather.models.Wind
import com.deepa.weather.repositories.WeatherRepository
import com.deepa.weather.viewmodels.WeatherViewMode
import com.deepa.weather.viewmodels.WeatherViewModel
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.Type


@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {
    val COORD1 = Coord(11.11, 22.22)
    val COORD2 = Coord(33.33, 44.44)
    val searchCoord = Coord(55.55, 66.66)
    val SearchLoc = "London"
    lateinit var coordCaptor: ArgumentCaptor<Coord>

    val gson: Gson = Gson()

    val weatherLocationManager: WeatherLocationManager = mockk()
    val weatherRepository :WeatherRepository = mockk()
    val sharedPreferenceHelper: SharedPreferenceHelper = mockk()

    lateinit var weatherViewModel: WeatherViewModel

    @Before
    fun setup() {
        mockkStatic(Looper::class)

        val looper = mockk<Looper> {
            every { thread } returns Thread.currentThread()
        }

        every { Looper.getMainLooper() } returns looper

        coordCaptor = ArgumentCaptor.forClass(Coord::class.java)
        setupDefaultMocks()

        weatherViewModel =
            WeatherViewModel(weatherLocationManager, weatherRepository, sharedPreferenceHelper)
    }

    @After
    fun after(){
        unmockkAll()
    }


    @Test
    fun noLocationPermission_initialState_EmptyDetail() {
        setupEmptyRepository()
        verifyIsDetailMode(null)
    }

    @Test
    fun userSearchLocation_updatesViewModeWithRecentSearch(){
        weatherViewModel.userSearchLocation()

        verifyIsDetailMode(searchCoord)
    }

    @Test
    fun onViewResumed_refreshesData() = runBlocking{
        val firstData = getAllWeatherResults(COORD1 , COORD2)
        coEvery { weatherRepository.getAllWeather() } returns firstData
        weatherViewModel.onViewResumed()
        verifyDataRefreshed(firstData, weatherViewModel.viewState?.value?.data)

        val coord3 = Coord(88.99, 99.88)
        val coord4 = Coord(55.66, 77.88)
        val allData = getAllWeatherResults(coord3, coord4)
        setupWeatherRepository(coord3, coord4)
        coEvery { weatherRepository.getAllWeather() } returns allData

        weatherViewModel.onViewResumed()

        verifyDataRefreshed(allData, weatherViewModel.viewState?.value?.data)
    }

    private fun verifyIsDetailMode(coord: Coord?){
        var currMode = getIfDetailMode()
        assertNotNull(getViewMode())
        assertTrue(getViewMode() is WeatherViewMode.Detail)
        assertEquals(coord, currMode?.coord)
    }

    private fun verifyDataRefreshed(
        expected: List<Resource<WeatherData>>?,
        actual: List<Resource<WeatherData>>?){
        assertNotNull(expected)
        assertNotNull(actual)
        val gson = Gson()
        val expectedData = gson.toJson(expected)
        val actualData = gson.toJson(actual)
        assertEquals(expectedData, actualData)
    }

    private fun getViewMode(): WeatherViewMode? {
        return weatherViewModel.viewState.value?.viewMode
    }

    private fun getIfDetailMode(): WeatherViewMode.Detail? {
        val viewMode = weatherViewModel.viewState.value?.viewMode
        return if (viewMode is WeatherViewMode.Detail) viewMode else null
    }

    private fun setupDefaultMocks() {
        every{sharedPreferenceHelper.onLocationPermissionDialogShown()} returns Unit
        every{sharedPreferenceHelper.isLocationPermissionDialogShown()} returns true
        every{weatherLocationManager.isLocationPermissionGranted()} returns true
        setupWeatherRepository()
    }

    private fun setupWeatherRepository(coord1: Coord = COORD1, coord2: Coord= COORD2) {
        val allData =  getAllWeatherResults(coord1, coord2)
        coEvery{weatherRepository.getWeather(any(), any())} returns allData[0]
        coEvery {  weatherRepository.getAllWeather()} returns allData

        val lastSearchData = Resource.success(getWeatherData(searchCoord))
        coEvery{weatherRepository.getWeatherForLastSearched()} returns lastSearchData
    }

    private fun getAllWeatherResults(coord1: Coord, coord2: Coord): List<Resource<WeatherData>>{
        val allData = ArrayList<Resource<WeatherData>>()
        val data1 = Resource.success(getWeatherData(coord1?:COORD1))
        val data2 = Resource.success(getWeatherData(coord2?:COORD2))

        coord1?.let{allData.add(data1)}
        coord2?.let{allData.add(data2)}
        return allData

    }

    private fun setupEmptyRepository(){
        coEvery {  weatherRepository.getAllWeather()} returns listOf()

    }
    private fun getWeatherData(coor: Coord = COORD1, location: String = "Palo Alto"): WeatherData {
        return WeatherData(coor, true, getCurrentWeather(coor, location))
    }

    private fun getCurrentWeather(coor: Coord, location: String): CurrentWeather {
        return CurrentWeather(
            coor,
            listOf(),
            12345L,
            location,
            "6740",
            Main(temp = 87.11, 72.22, 90.33, 54, 1111),
            4,
            Wind(5.1),
            Rain(6.1),
            Clouds(7)
        )
    }

    private fun <T> getJson(key: String, data: T?): String {
        val gson = Gson()
        return gson.toJson(data)
    }

    fun <T> getData(key: String, type: Type): T?{
        return gson.fromJson(key, type)
    }


}