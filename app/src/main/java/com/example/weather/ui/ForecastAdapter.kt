package com.example.weather.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.data.model.DailyItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.MyViewHolder>() {

    var items = ArrayList<DailyItem>()

    fun setUpdatedData(item: ArrayList<DailyItem>) {
        this.items = item
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val temperatureForecast=view.findViewById<TextView>(R.id.temperatureForecast)
        val hightemperatureForecast = view.findViewById<TextView>(R.id.highTemperatureForecast)
        val lowtemperatureForecast = view.findViewById<TextView>(R.id.lowTemperatureForecast)
        val dateTimeForecast = view.findViewById<TextView>(R.id.dateTimeForecast)
        val iconForecast = view.findViewById<ImageView>(R.id.iconForecast)
        fun bind(data: DailyItem) {
            val temp = data.temp
            // temperatureForecast.text=temp?.day.toString()
            hightemperatureForecast.text = temp?.max.toString()
            lowtemperatureForecast.text = temp?.min.toString()

            val WeatherItem = data?.weather?.get(0)
            val iconid = WeatherItem?.icon
            val imageUrl = "https://openweathermap.org/img/wn/$iconid@2x.png"
            Glide.with(itemView).load(imageUrl)
                .error(R.drawable.ic_baseline_cloud_circle_24)
                .into(iconForecast)


            val dt = data.dt
            val sdf = SimpleDateFormat("d EEEE, hh:mm a", Locale.getDefault())
            var date = sdf.format(dt?.times(1000)!!)
            dateTimeForecast.text = date.toString()


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rowforecast_fragment, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items.get(position))
    }

    override fun getItemCount(): Int {
        return items.size
    }
}