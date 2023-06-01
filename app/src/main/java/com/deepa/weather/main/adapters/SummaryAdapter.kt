package com.deepa.weather.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.deepa.weather.R
import com.deepa.weather.data.network.Resource
import com.deepa.weather.data.network.Status
import com.deepa.weather.models.Coord
import com.deepa.weather.models.WeatherData

class SummaryAdapter(private val onItemClickListener: OnSummaryItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var summaryList: List<Resource<WeatherData>> = listOf()

    class SummaryViewHolder(itemView: View): ViewHolder(itemView){
        private var cityView : TextView
        init{
            cityView = itemView.findViewById(R.id.city)
        }

        fun bind(weatherDataResource: Resource<WeatherData>, pos: Int, onItemClickListener: OnSummaryItemClickListener){
            if(weatherDataResource.status != Status.SUCCESS) return
            val weatherData = weatherDataResource.data?:return
            cityView.text = weatherData.currentWeather?.cityName
            itemView.setOnClickListener {
                onItemClickListener.onItemClicked(weatherData.coor)
            }
        }
    }

    fun updateList(summaryList: List<Resource<WeatherData>>){
        // TODO: use DiffUtil to update only the changed data.
        this.summaryList = summaryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val summaryItem = LayoutInflater.from(parent.context).inflate(R.layout.item_summary_city, parent, false)
        return SummaryViewHolder(summaryItem)
    }

    override fun getItemCount(): Int {
        return summaryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val summaryViewHolder = holder as SummaryViewHolder
        summaryViewHolder.bind(summaryList[position],  position, onItemClickListener)
    }

}

interface OnSummaryItemClickListener{
    fun onItemClicked(coord: Coord)
}