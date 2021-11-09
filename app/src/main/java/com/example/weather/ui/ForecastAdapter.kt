package com.example.weather.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.data.model.ForecastDailyModel

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.MyViewHolder>() {

    var items = ArrayList<ForecastDailyModel>()

    fun setForecastData(item: ArrayList<ForecastDailyModel>) {
        items = item
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hightemperatureForecast = itemView.findViewById<TextView>(R.id.highTemperatureForecast)
        val lowtemperatureForecast = itemView.findViewById<TextView>(R.id.lowTemperatureForecast)
        val dateTimeForecast = itemView.findViewById<TextView>(R.id.dateTimeForecast)
        var iconForecast = itemView.findViewById<ImageView>(R.id.iconForecast)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rowforecast_fragment, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.hightemperatureForecast.text = items[position].max.toString()
        holder.dateTimeForecast.text = items[position].dt.toString()
        holder.lowtemperatureForecast.text = items[position].min.toString()

        val iconId = items[position].icon
        val imageUrl = "https://openweathermap.org/img/wn/$iconId@2x.png"
        Glide.with(holder.iconForecast.context).load(imageUrl)
            .error(R.drawable.ic_baseline_cloud_circle_24).into(holder.iconForecast)

    }

    override fun getItemCount(): Int {
        return items.size
    }
}