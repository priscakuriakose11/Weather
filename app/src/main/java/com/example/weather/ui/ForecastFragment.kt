package com.example.weather.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.data.model.DailyItem
import com.example.weather.databinding.ForecastFragmentBinding
import com.example.weather.databinding.TodayFragmentBinding

class ForecastFragment : Fragment() {
    private lateinit var forecastAdapter: ForecastAdapter
    private var _binding: ForecastFragmentBinding? = null
    private val binding get() = _binding!!

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
            viewModel.unitsLiveData.observe(viewLifecycleOwner)
            { unitsLiveData ->
                val unit = unitsLiveData

                initViewModel(view)
                if (lat != null && lon != null) {
                    initViewModel(lat, lon, unit)
                }
            }
        }


    }

    private fun initViewModel(view: View) {
        val forecastRecyclerView = view.findViewById<RecyclerView>(R.id.ForecastRecyclerView)
        forecastRecyclerView.layoutManager = LinearLayoutManager(activity)
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        forecastRecyclerView.addItemDecoration(decoration)


        forecastAdapter = ForecastAdapter()
        forecastRecyclerView.adapter = forecastAdapter
    }

    private fun initViewModel(lat: Double, lon: Double, unit: String) {
        viewModel.getForecastWeather(lat, lon, unit)
        viewModel.forecastWeatherLiveData.observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Log.d("T", "Network call failed")

            }
            forecastAdapter.setUpdatedData(response?.daily as ArrayList<DailyItem>)
        }
    }

}