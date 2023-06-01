package com.deepa.weather.search

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.deepa.weather.MainActivity
import com.deepa.weather.R
import com.deepa.weather.databinding.ActivitySearchBinding
import com.deepa.weather.models.Coord
import com.deepa.weather.ui.CommonDialog
import com.deepa.weather.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var countryNameTextView: EditText
    private lateinit var cityNameTextView: EditText
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        countryNameTextView = findViewById(R.id.searchCountryName)
        cityNameTextView = findViewById(R.id.searchCityName)

        searchViewModel.viewState.observe(this, Observer { updateUi(it) })

        setupObservers()
    }

    private fun setupObservers(){
        binding.btnSearch.setOnClickListener {
            searchViewModel.onSearchStarted(
                cityNameTextView.text.toString(),
                countryNameTextView.text.toString()
            )
        }
        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun updateUi(searchViewState: SearchViewState){
        if(searchViewState.searchState is SearchState.Searching){
            Toast.makeText(this, "Searching", Toast.LENGTH_LONG)
        }
        else if(searchViewState.searchState is SearchState.SearchResult){
            val result = searchViewState.searchState
            if(result.results.isEmpty()){
                showError("No results. Check the city name and try again. ")
            }
            else if(result.results.size == 1){
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                val coord = Coord(result.results[0].lat, result.results[0].lon)
                val bundle = MainActivity.getBundleForLastSearch(coord)
                intent.putExtras(bundle)
                finish()
                startActivity(intent)
            }
            else{
                // TODO: Show dialog to allow user to select the city from multiple choice.
                showError("Multiple cities found. Case not handled yet. Narrow the search by specifying neighboring cities. ")
            }
            Toast.makeText(this, "got result", Toast.LENGTH_LONG)
        }
        else if(searchViewState.searchState is SearchState.Error){
            showError("Something went wrong. Try again.")
        }
    }

    private fun showError(message: String){
        val dialogFragment = CommonDialog()

        val bundle = Bundle()
        bundle.putString("message", message)
        dialogFragment.arguments = bundle

        val ft = supportFragmentManager.beginTransaction()
        val prev: Fragment? = supportFragmentManager.findFragmentByTag("CommonErrorDialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        dialogFragment.show(ft, "CommonErrorDialog")

    }
}