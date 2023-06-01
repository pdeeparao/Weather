package com.deepa.weather.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.deepa.weather.R
import com.deepa.weather.data.network.Resource
import com.deepa.weather.data.network.Status
import com.deepa.weather.databinding.ItemDetailCityBinding
import com.deepa.weather.glide.AppGlideAppModule
import com.deepa.weather.models.Coord
import com.deepa.weather.models.GeoLocation
import com.deepa.weather.models.WeatherData
import com.deepa.weather.network.IMAGE_URL
import com.deepa.weather.util.WeatherUtils
import com.deepa.weather.viewmodels.WeatherViewMode
import java.util.Locale
import javax.inject.Inject

class DetailsAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var detailsList: List<Resource<WeatherData>> = listOf()
    var currentLocation:GeoLocation? = null
    var currentFocusPos: Int = 0
    lateinit var binding: ItemDetailCityBinding

    class DetailsViewHolder(binding: ItemDetailCityBinding): ViewHolder(binding.root){
        private var binding:ItemDetailCityBinding
        init{
            this.binding  = binding
        }

        fun bind(weatherDataResource: Resource<WeatherData>, pos: Int){
            if(weatherDataResource.status == Status.ERROR) return
            val weatherData = weatherDataResource.data?:return
            val currWeather = weatherData.currentWeather

            if(currWeather == null){
                binding.city.text = "-"
                binding.currTemp.text = "-"
                binding.loTemp.text = "-"
                binding.hiTemp.text ="-"
            }
            else{
                val string =currWeather.weather[0].icon
                val imageUrl = String.format(Locale.US, IMAGE_URL, string)
                Glide.with(itemView.context).load(imageUrl).into(binding.tempIcon)
                val condition =if(currWeather.weather.isNotEmpty()){
                    currWeather.weather[0].main }else "-"
                binding.condition.text = condition
                binding.city.text = currWeather.cityName
                binding.description.text = currWeather.weather[0].description
                binding.currTemp.text = itemView.resources.getString(R.string.temperature, currWeather.main.temp.toString() )
                binding.loTemp.text = itemView.resources.getString(R.string.temperature, currWeather.main.low.toString())
                binding.hiTemp.text = itemView.resources.getString(R.string.temperature, currWeather.main.high.toString())
                binding.wind.text = itemView.resources.getString(R.string.wind, currWeather.wind.speed.toString())
                binding.rain.text = itemView.resources.getString(R.string.rain, currWeather.rain?.hourly?.toString()?:0)
                binding.cloudiness.text = itemView.resources.getString(R.string.cloudiness, currWeather.clouds.all.toString())
                if(weatherData.isCurrentLocation){
                    binding.imgLocationType.setImageResource(R.drawable.ic_navigation)
                }
                else{
                    binding.imgLocationType.setImageResource(R.drawable.ic_pin)
                }

            }
        }
    }



    fun updateList(detailsList: List<Resource<WeatherData>> ){
        // use DiffUtil to update only the changed data.
        this.detailsList = detailsList
    }


    fun currentLocation(currentLocation: GeoLocation){
        // TODO: use DiffUtil to update only the changed data.
        this.currentLocation = currentLocation
        this.currentFocusPos = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemDetailCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val detailsViewHolder = holder as DetailsViewHolder
        detailsViewHolder.bind(detailsList[position],  position)
    }

}
