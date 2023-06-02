package com.deepa.weather

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.deepa.weather.databinding.ActivityMainBinding
import com.deepa.weather.main.DetailsFragment
import com.deepa.weather.models.Coord
import com.deepa.weather.search.SearchActivity
import com.deepa.weather.viewmodels.WeatherViewModel
import com.deepa.weather.viewmodels.WeatherViewStateData
import com.deepa.weather.viewmodels.WeatherViewType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val viewType = "ShowDetailsForLocation"
        private const val lastSearchCoords = "LastSearchCoord"

        fun getBundle(weatherViewType: WeatherViewType): Bundle {
            val bundle = Bundle()
            bundle.putInt(viewType, weatherViewType.value)
            return bundle
        }

        fun getBundleForLastSearch(coord: Coord): Bundle {
            val bundle = Bundle()
            bundle.putBoolean(lastSearchCoords, true)
            return bundle
        }

        fun isForLastSearch(bundle: Bundle?): Boolean {
            return bundle?.getBoolean(lastSearchCoords, false) ?: false
        }

        fun getViewType(bundle: Bundle?): WeatherViewType {
            return WeatherViewType.getWeatherViewType(bundle?.getInt(viewType))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewType = getViewType(intent.extras)

        if (isForLastSearch(intent.extras)) {
            weatherViewModel.userSearchLocation()
        }
        if (savedInstanceState == null) {
            // Let's first dynamically add a fragment into a frame container
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_holder, DetailsFragment(), "summary").commit()
        }

        setupListeners()

        weatherViewModel.setupDefaultView()
        weatherViewModel.viewState.observe(this) { updateUi(it) }
    }

    override fun onResume() {
        super.onResume()
        weatherViewModel.onViewResumed()
    }

    private fun setupListeners() {
        binding.btnSearchCity.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        binding.btnLocationPermission.setOnClickListener {
            Intent(ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${packageName}")).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(this)
            }
        }
        //TODO: Is this needed.
//        binding.toggleView.setOnClickListener{
//            val currState = weatherViewModel.viewState.value?: return@setOnClickListener
//            if(currState.inputState is InputState.DetailedView){
//                weatherViewModel.setViewType(WeatherViewType.Summary)
//            }
//        }
    }

    private fun updateUi(currState: WeatherViewStateData) {
//        when(currState.viewType){
//            WeatherViewType.Detail, WeatherViewType.LastSearchDetail->{
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_fragment_holder, DetailsFragment(), "details").commit()
//                if(currState.locationPermissionGranted){
//                    binding.toggleView.text = "Summary"
//                }
//            }
//            WeatherViewType.CurrentLocation->{
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_fragment_holder, DetailsFragment(), "details").commit()
//                if(currState.locationPermissionGranted){
//                    binding.toggleView.text = "Summary"
//                }
//            }
//            WeatherViewType.Summary->{
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_fragment_holder, SummaryFragment(), "details").commit()
//                binding.toggleView.visibility = View.GONE
//            }
//            else->{
//            }
//        }
//        updateToggleButton(currState)
    }

//    private fun updateToggleButton(currState: WeatherViewStateData){
//        if(currState.locationPermissionGranted){
//            binding.btnLocationPermission.visibility = View.GONE
//            binding.toggleView.visibility = View.VISIBLE
//        }
//        when(currState.inputState){
//            is InputState.DetailedView->{
//                if(currState.locationPermissionGranted){
//                    binding.toggleView.text = "Summary"
//                }
//            }
//            is InputState.SummaryView->{
//                binding.toggleView.visibility = View.GONE
//            }
//            else->{
//            }
//        }
//
//    }

}
