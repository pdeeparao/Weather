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
import com.deepa.weather.main.adapters.OnSummaryItemClickListener
import com.deepa.weather.main.adapters.SummaryAdapter
import com.deepa.weather.main.adapters.VerticalSpaceDecorator
import com.deepa.weather.models.Coord
import com.deepa.weather.models.WeatherData
import com.deepa.weather.viewmodels.WeatherViewModel
import com.deepa.weather.viewmodels.WeatherViewStateData
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SummaryFragment : Fragment() {
    private lateinit var summaryListView: RecyclerView
    private lateinit var summaryAdapter: SummaryAdapter
    private val viewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_summary, container, false)

        summaryListView = view?.findViewById(R.id.cities)!!
        summaryListView.layoutManager = LinearLayoutManager(activity)
        summaryAdapter = SummaryAdapter(object : OnSummaryItemClickListener {
            override fun onItemClicked(coord: Coord) {
//                viewModel.onClickedOnViewDetails(coord)
            }
        })
        summaryListView.adapter = summaryAdapter
        summaryListView.addItemDecoration(VerticalSpaceDecorator(10))
        viewModel.viewState.observe(requireActivity()){updateUi(it.data)}

        viewModel.viewState.value?.let{
            showPermissionDialog(it)
        }
        return view
    }

    private fun updateUi(weatherDataList: List<Resource<WeatherData>>){
        summaryAdapter.updateList(weatherDataList)

    }

    private fun showPermissionDialog(currState: WeatherViewStateData){
        if(!currState.locationPermissionGranted && !currState.locationPermissionRequested)
        {
            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        viewModel.onPermissionGranted()
                    }
                    permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        // Only approximate location access granted.
                    } else -> {
                    // No location access granted.
                }
                }
            }
            locationPermissionRequest.launch(arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION))
            viewModel.onPermissionDialogShown()
        }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SummaryFragment.
         */
        fun newInstance() = SummaryFragment()
    }
}