package com.example.weather.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.databinding.TodayFragmentBinding
import java.text.SimpleDateFormat
import java.util.*

class TodayFragment() : Fragment() {
    private var _binding: TodayFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodayViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TodayFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        getToday(binding.enterCity.text.toString())

        binding.go.setOnClickListener {
            getToday(binding.enterCity.text.toString())
            var oldCity = binding.enterCity.text.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            binding.temperature.text = main?.temp.toString()
            binding.highTemperature.text = main?.tempMax.toString()
            binding.lowTemperature.text = main?.tempMin.toString()
            val sdf = SimpleDateFormat("d MMMM, hh:mm a", Locale.getDefault())
            val date = sdf.format(Date()).toString()
            binding.dateTime.text = date
            val WeatherItem = response?.weather?.get(0)
            binding.iconText.text = WeatherItem?.description
            val iconid = WeatherItem?.icon
            Log.d("Icon", "$iconid")
            val imageUrl = "https://openweathermap.org/img/wn/$iconid@2x.png"
            Glide.with(this).load(imageUrl)
                .error(R.drawable.ic_baseline_cloud_circle_24)
                .into(binding.icon)
        }

    }


}
