package com.example.weather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.data.database.Cities
import com.example.weather.databinding.TodayFragmentBinding
import com.example.weathersampleapp.data.utils.Constants.Companion.TEXT_CONTENTS
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.text.SimpleDateFormat
import java.util.*

class TodayFragment() : Fragment() {
    private var _binding: TodayFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CitiesViewModel by activityViewModels()

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var geocoder: Geocoder

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_CONTENTS, binding.place.text.toString())

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (savedInstanceState != null) {
            binding.place.text = savedInstanceState.getString(TEXT_CONTENTS, "")
        }

        _binding = TodayFragmentBinding.inflate(inflater, container, false)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        viewModel.readAll.observe(viewLifecycleOwner, {

            if (it.isNotEmpty()) {
                val allCities = mutableListOf<String>()
                for (i in it) {
                    allCities.add(i.name!!)
                }
                Log.d("cities", "${it[0]}")
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.dropdown_cities, allCities)
                binding.enterCity.setAdapter(arrayAdapter)
            }

        })




        viewModel.unitsLiveData.observe(viewLifecycleOwner)
        { unitsLiveData ->

            val unit = unitsLiveData
            if(unit=="Metric")
            {   binding.highTemperatureUnit.text = "°C"
                binding.lowTemperatureUnit.text = "°C"
                binding.temperatureUnit.text="°C"}
            if (unit=="Imperial")
            {   binding.lowTemperatureUnit.text="°F"
                binding.highTemperatureUnit.text="°F"
                binding.temperatureUnit.text="°F"}


            viewModel.weatherLiveData.observe(viewLifecycleOwner){
                    response->
                if (response!=null){
                    binding.place.text=response.name
                }
            }

            if (binding.place.text.isEmpty()) {
                fetchlocation(unit)
            }



            binding.location.setOnClickListener {
                fetchlocation(unit)
            }
            if (!binding.place.text.isEmpty()) {
                getToday(binding.place.text.toString(), unit)
            }

            binding.go.setOnClickListener {
                getToday(binding.enterCity.text.toString(), unit)

            }
        }
    }

    private fun fetchlocation(unit: String) {


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        } else {

            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val task: Task<Location> = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener {
                Log.d("Location", "Success")
                if (it != null) {
                    Log.d("location", "Not Null")
                    val address = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    val city = address.get(0).getAdminArea()
                    //var city1 = address.get(0).getLocality()


                    binding.place.text = city
                    getToday(city, unit)


                } else {
                    Log.d("location", "Null")
                }
            }
        }


    }


    private fun getToday(city: String, unit: String) {
        Log.d("getToday", "Success")
        viewModel.getCurrentWeather(city, unit)
        viewModel.weatherLiveData.observe(viewLifecycleOwner)
        { response ->
            if (response == null) {
                Log.d("Response", "Null")
                Toast.makeText(requireContext(), "$city is not city!!!", Toast.LENGTH_SHORT).show()
            }
            val main = response?.main
            binding.place.text = response?.name
            binding.temperature.text = main?.temp.toString()
            binding.highTemperature.text = main?.tempMax.toString()
            binding.lowTemperature.text = main?.tempMin.toString()
            val sdf = SimpleDateFormat("d MMMM, hh:mm a", Locale.getDefault())
            val date = sdf.format(Date()).toString()
            binding.dateTime.text = date
            val WeatherItem = response?.weather?.get(0)
            binding.iconText.text = WeatherItem?.description
            val iconid = WeatherItem?.icon

            val imageUrl = "https://openweathermap.org/img/wn/$iconid@2x.png"
            Glide.with(this).load(imageUrl)
                .error(R.drawable.ic_baseline_cloud_circle_24)
                .into(binding.icon)
            binding.favorite.setOnClickListener {
                if (response != null && !binding.place.text.isEmpty()) {
                    val coord = response?.coord
                    val cityInfo = Cities(response?.id, response?.name, coord?.lon, coord?.lat)

                    viewModel.addCity(cityInfo)

                    Toast.makeText(requireContext(), "Favorited!!!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Enter Valid City !!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }


}
