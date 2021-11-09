package com.example.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.data.database.Cities
import com.example.weather.databinding.TodayFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

class TodayFragment() : Fragment() {
    private var _binding: TodayFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CitiesViewModel by activityViewModels()

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = TodayFragmentBinding.inflate(inflater, container, false)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InternetCheck(object : InternetCheck.Consumer {
            override fun accept(internet: Boolean?) {
                Log.d("Internet", "$internet")
                if(internet==false) {
                    Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()
                }
            }
        })

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




        viewModel.unitData.observe(viewLifecycleOwner)
        { unitsLiveData ->

            val unit = unitsLiveData

            viewModel.weatherLiveData.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    binding.place.text = response.name
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
                    Toast.makeText(requireContext(), "Set Location", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }


    @SuppressLint("SetTextI18n")
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
            if (unit==getString(R.string.metric)) {
                binding.temperature.text = main?.temp.toString() + getString(R.string.celsiusSymbol)
                binding.highTemperature.text = main?.tempMax.toString() + getString(R.string.celsiusSymbol)
                binding.lowTemperature.text = main?.tempMin.toString()  + getString(R.string.celsiusSymbol)
            }
            if (unit==getString(R.string.imperial)) {
                binding.temperature.text = main?.temp.toString() + getString(R.string.fahrenheitSymbol)
                binding.highTemperature.text = main?.tempMax.toString() + getString(R.string.fahrenheitSymbol)
                binding.lowTemperature.text = main?.tempMin.toString()  + getString(R.string.fahrenheitSymbol)
            }
            val sdf = SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault())
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

    internal class InternetCheck(private val mConsumer: Consumer) :
        AsyncTask<Void, Void, Boolean>() {
        interface Consumer {
            fun accept(internet: Boolean?)
        }

        init {
            execute()
        }

        override fun doInBackground(vararg voids: Void): Boolean? {
            try {
                val sock = Socket()
                sock.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                sock.close()
                return true
            } catch (e: IOException) {
                return false
            }

        }

        override fun onPostExecute(internet: Boolean?) {
            mConsumer.accept(internet)
        }
    }

}
