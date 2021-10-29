package com.example.weather.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.weather.R
import java.text.SimpleDateFormat
import java.util.*

class TodayFragment : Fragment() {

    /*companion object {
        fun newInstance() = TodayFragment()
    }*/

    private lateinit var viewModel: TodayViewModel
    lateinit var temperature: TextView
    lateinit var enterCity: EditText
    lateinit var go: Button
    lateinit var dateTime: TextView
    lateinit var highTemperature: TextView
    lateinit var lowTemperature: TextView
    lateinit var iconText: TextView
    lateinit var icon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.today_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        temperature = view.findViewById(R.id.temperature)
        enterCity = view.findViewById(R.id.enterCity)
        go = view.findViewById(R.id.go)
        dateTime = view.findViewById(R.id.dateTime)
        highTemperature = view.findViewById(R.id.highTemperature)
        lowTemperature = view.findViewById(R.id.lowTemperature)
        iconText = view.findViewById(R.id.iconText)
        icon = view.findViewById(R.id.icon)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TodayViewModel::class.java)



        getToday(enterCity.text.toString())

        go.setOnClickListener {
            getToday(enterCity.text.toString())
        }


    }


    private fun getToday(city: String) {
        viewModel.getCurrentWeather(city)
        viewModel.weatherLiveData.observe(viewLifecycleOwner)
        { response ->
            if (response == null) {
                Log.d("T", "Network call failed")

            }
            val main = response?.main

            val clouds = response?.clouds
            temperature.text = main?.temp.toString()
            highTemperature.text = main?.tempMax.toString()
            lowTemperature.text = main?.tempMin.toString()
            val sdf = SimpleDateFormat("d MMMM, hh:mm a", Locale.getDefault())
            val date = sdf.format(Date()).toString()
            dateTime.text = date
            val WeatherItem = response?.weather?.get(0)
            iconText.text = WeatherItem?.description
            val iconid = WeatherItem?.icon
            Log.d("Icon", "$iconid")
            val imageUrl = "https://openweathermap.org/img/wn/$iconid@2x.png"
            Glide.with(this).load(imageUrl)
                .error(R.drawable.ic_baseline_cloud_circle_24)
                .into(icon)
        }

    }
}