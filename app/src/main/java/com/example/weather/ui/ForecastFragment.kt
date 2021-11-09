package com.example.weather.ui

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.data.model.ForecastDailyModel
import com.example.weather.databinding.ForecastFragmentBinding

class ForecastFragment : Fragment() {
    private lateinit var forecastAdapter: ForecastAdapter
    private var _binding: ForecastFragmentBinding? = null
    private val binding get() = _binding!!
    private var daily = mutableListOf<ForecastDailyModel>()

    private val viewModel: CitiesViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ForecastFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.weatherLiveData.observe(viewLifecycleOwner)
        { response ->
            val coord = response?.coord
            val lat = coord?.lat
            val lon = coord?.lon
            viewModel.unitData.observe(viewLifecycleOwner)
            { unitsLiveData ->
                val unit = unitsLiveData
                initRecyclerView(view)
                if (lat != null && lon != null) {
                    getForecastWeather(lat, lon, unit)
                }
            }
        }


    }

    private fun initRecyclerView(view: View) {
        val forecastRecyclerView = view.findViewById<RecyclerView>(R.id.ForecastRecyclerView)
        forecastRecyclerView.layoutManager = LinearLayoutManager(activity)
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        forecastRecyclerView.addItemDecoration(decoration)


        forecastAdapter = ForecastAdapter()
        forecastRecyclerView.adapter = forecastAdapter
    }

    @SuppressLint("SimpleDateFormat")
    private fun getForecastWeather(lat: Double, lon: Double, unit: String) {
        var unitSymbol: String
        viewModel.getForecastWeather(lat, lon, unit)
        viewModel.forecastWeatherLiveData.observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Log.d("T", "Network call failed")
            } else {
                val maxDays = response?.daily?.lastIndex
                for (i in 0..maxDays!!) {
                    if (unit == getString(R.string.metric)) {
                        unitSymbol = getString(R.string.celsiusSymbol)
                    } else {
                        unitSymbol = getString(R.string.fahrenheitSymbol)
                    }
                    val min = response?.daily?.get(i)?.temp?.min.toString() + unitSymbol
                    val max = response?.daily?.get(i)?.temp?.max.toString() + unitSymbol


                    val dt1 = response?.daily?.get(i)?.dt
                    val date = java.time.format.DateTimeFormatter.ISO_INSTANT
                        .format(java.time.Instant.ofEpochSecond(dt1!!.toLong()))
                    val parser = SimpleDateFormat(getString(R.string.dateFormatObtained))
                    val formatter = SimpleDateFormat(getString(R.string.dateFormatForecast))
                    val output = formatter.format(parser.parse(date))
                    val dt = output

                    val iconId = response?.daily?.get(i)?.weather?.get(0)?.icon

                    daily.add(ForecastDailyModel(iconId, min, max, dt))
                }
                forecastAdapter.setForecastData(daily as ArrayList<ForecastDailyModel>)
            }

        }


    }
}

