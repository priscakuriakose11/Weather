package com.example.weather.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.data.model.ForecastDailyModel
import com.example.weathersampleapp.data.utils.Constants.Companion.IMAGE_URL

class ForecastAdapter(daily: MutableList<ForecastDailyModel>) :
    RecyclerView.Adapter<ForecastAdapter.MyViewHolder>() {

    var items = daily

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val highTemperatureForecast: TextView =
            itemView.findViewById<TextView>(R.id.highTemperatureForecast)
        val lowTemperatureForecast: TextView =
            itemView.findViewById<TextView>(R.id.lowTemperatureForecast)
        val dateTimeForecast: TextView = itemView.findViewById<TextView>(R.id.dateTimeForecast)
        var iconForecast: ImageView = itemView.findViewById<ImageView>(R.id.iconForecast)
        var updateTime:TextView = itemView.findViewById(R.id.updateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rowforecast_fragment, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.highTemperatureForecast.text = items[position].max
        holder.lowTemperatureForecast.text = items[position].min
        holder.dateTimeForecast.text = items[position].dt.toString()
        holder.updateTime.text = items[position].update.toString()
        val iconId = items[position].icon
        Glide.with(holder.iconForecast.context).load(IMAGE_URL.format("$iconId"))
            .error(R.drawable.ic_baseline_cloud_circle_24).into(holder.iconForecast)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}