package com.deepa.weather.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deepa.weather.R
import com.deepa.weather.data.network.Resource
import com.deepa.weather.main.adapters.DetailsAdapter
import com.deepa.weather.main.adapters.VerticalSpaceDecorator
import com.deepa.weather.models.Coord
import com.deepa.weather.models.WeatherData
import com.deepa.weather.viewmodels.WeatherViewMode
import com.deepa.weather.viewmodels.WeatherViewModel
import com.deepa.weather.viewmodels.WeatherViewStateData
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var detailsListView: RecyclerView
    private lateinit var detailsAdapter: DetailsAdapter

    private val viewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        detailsListView = view?.findViewById(R.id.cities)!!
        detailsListView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        detailsAdapter = DetailsAdapter()
        detailsListView.adapter = detailsAdapter
        detailsListView.addItemDecoration(VerticalSpaceDecorator(10))
        viewModel.viewState.observe(requireActivity()) { updateUi(it, it.data) }
        viewModel.viewState.value?.let {
            showPermissionDialog(it)
        }
        return view
    }

    private fun updateUi(
        viewmodelStateData: WeatherViewStateData,
        weatherDataList: List<Resource<WeatherData>>
    ) {
        detailsAdapter.updateList(weatherDataList)
        val viewMode = if (viewmodelStateData.viewMode is WeatherViewMode.Detail) {
            viewmodelStateData.viewMode
        } else null
        val coord = viewMode?.coord
        var focusIdx = 0

        for (i in weatherDataList.indices) {
            if (isWeatherDataForLocation(coord, weatherDataList[i])) {
                focusIdx = i
            }
        }
        detailsListView.scrollToPosition(focusIdx)
        detailsAdapter.notifyDataSetChanged()
    }

    private fun isWeatherDataForLocation(coord: Coord?, data: Resource<WeatherData>): Boolean {
        val dataCoor = data.data?.coor
        if (coord == null && dataCoor == null) return false
        if (coord == null || dataCoor == null) return false
        return (coord.lat == dataCoor.lat && coord.lon == coord.lon)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment DetailsFragment.
         */
        fun newInstance() = DetailsFragment()
    }


    private fun showPermissionDialog(currState: WeatherViewStateData) {
        if (!currState.locationPermissionGranted && !currState.locationPermissionRequested) {
            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        false
                    ) -> {
                        viewModel.onPermissionGranted()
                    }

                    permissions.getOrDefault(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        false
                    ) -> {
                        // Only approximate location access granted.
                    }

                    else -> {
                        // No location access granted.
                    }
                }
            }
            locationPermissionRequest.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            viewModel.onPermissionDialogShown()
        }

    }
}