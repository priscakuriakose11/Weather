package com.example.weather.ui

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
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
        val highTemperatureForecastUnit =
            view.findViewById<TextView>(R.id.highTemperatureForecastUnit)
        val lowTemperatureForecastUnit =
            view.findViewById<TextView>(R.id.lowTemperatureForecastUnit)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: DailyItem) {
            val temp = data.temp
            // temperatureForecast.text=temp?.day.toString()
            hightemperatureForecast.text = temp?.max.toString()
            if (hightemperatureForecast.text.toString() <= "56") {
                highTemperatureForecastUnit.text = "°C"
                lowTemperatureForecastUnit.text = "°C"
            } else {
                highTemperatureForecastUnit.text = "°F"
                lowTemperatureForecastUnit.text = "°F"
            }
            lowtemperatureForecast.text = temp?.min.toString()

            val WeatherItem = data?.weather?.get(0)
            val iconid = WeatherItem?.icon
            val imageUrl = "https://openweathermap.org/img/wn/$iconid@2x.png"
            Glide.with(itemView).load(imageUrl)
                .error(R.drawable.ic_baseline_cloud_circle_24)
                .into(iconForecast)


            val dt = data.dt
            if (dt != null) {
                val date = java.time.format.DateTimeFormatter.ISO_INSTANT
                    .format(java.time.Instant.ofEpochSecond(dt.toLong()))

                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val formatter = SimpleDateFormat("EE , dd LLL ")
                val output = formatter.format(parser.parse(date))
                dateTimeForecast.text = output
            }


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