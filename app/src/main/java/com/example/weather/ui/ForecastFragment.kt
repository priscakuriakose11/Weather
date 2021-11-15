package com.example.weather.ui

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.weather.R
import com.example.weather.data.model.ForecastDailyModel
import com.example.weather.databinding.ForecastFragmentBinding
import java.util.*
import android.os.Build
import android.widget.Toast


class ForecastFragment : Fragment() {

    private var _binding: ForecastFragmentBinding? = null
    private val binding get() = _binding!!
    private var daily = mutableListOf<ForecastDailyModel>()

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ForecastFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.reset()
        getLiveData()

        binding.swipeToRefresh.setOnRefreshListener {
            Log.d("Refresh", "Refreshed")
            binding.forecastRecyclerView.adapter.let { ForecastAdapter(daily).items.clear() }
            Toast.makeText(requireContext(), "Refreshed!!", Toast.LENGTH_SHORT).show()
            getLiveData()
        }
    }

    private fun getLiveData() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner)
        { response ->
            val coord = response?.coord
            val lat = coord?.lat
            val lon = coord?.lon

            viewModel.unitData.observe(viewLifecycleOwner)
            { unitsLiveData ->
                val unit = unitsLiveData
                if (lat != null && lon != null) {
                    getForecastWeather(lat, lon, unit)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getForecastWeather(lat: Double, lon: Double, unit: String) {
        var unitSymbol: String
        viewModel.getForecastWeather(lat, lon, unit)
        viewModel.forecastWeatherLiveData.observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Log.d("T", "Network call failed")
            } else {
                Log.d("T", "Network call success")
                val maxDays = response?.daily?.lastIndex
                for (i in 0..maxDays!!) {
                    if (unit == getString(R.string.imperial)) {
                        unitSymbol = getString(R.string.fahrenheitSymbol)
                    } else {
                        unitSymbol = getString(R.string.celsiusSymbol)
                    }
                    val min = response?.daily?.get(i)?.temp?.min.toString() + unitSymbol
                    val max = response?.daily?.get(i)?.temp?.max.toString() + unitSymbol

                    val sdf =
                        SimpleDateFormat(getString(R.string.dateFormatForecast), Locale.ENGLISH)
                    val dt = sdf.format(Date(response?.daily?.get(i)?.dt?.toLong()?.times(1000)!!))

                    val diff =
                        Date().time - Date(response?.current?.dt?.toLong()?.times(1000)!!).time
                    val update = updatedBefore(diff)
                    //Log.d("Time","${sdf.format(Date())},$dt")

                    val iconId = response?.daily?.get(i)?.weather?.get(0)?.icon

                    daily.add(ForecastDailyModel(iconId, min, max, dt, update))
                }
                binding.forecastRecyclerView.adapter = context.let {
                    ForecastAdapter(daily)
                }
                if (binding.swipeToRefresh.isRefreshing == true) {
                    binding.swipeToRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun updatedBefore(diff: Long): String {
        var timeInSeconds = (diff / 1000).toInt()
        val hours = timeInSeconds / 3600
        timeInSeconds = timeInSeconds - (hours * 3600)
        val minutes = timeInSeconds / 60
        timeInSeconds = timeInSeconds - (minutes * 60)
        val seconds = timeInSeconds
        var update: String
        if (seconds <= 0) {
            update = getString(R.string.now)
        } else if (seconds < 60) {
            update = "$seconds" + " " + getString(R.string.sec) + " " + getString(R.string.ago)
        } else if (seconds >= 60) {
            update = "$minutes" + " " + getString(R.string.min) + " " + getString(R.string.ago)
        } else if (minutes >= 60) {
            update = "$hours" + " " + getString(R.string.hr) + " " + getString(R.string.ago)
        } else {
            update = getString(R.string.day) + " " + getString(R.string.ago)
        }
        return update
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

