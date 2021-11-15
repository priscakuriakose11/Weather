package com.example.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
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
import com.example.weathersampleapp.data.utils.Constants.Companion.IMAGE_URL
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.text.SimpleDateFormat
import java.util.*

class TodayFragment() : Fragment() {
    private var _binding: TodayFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()

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

        if (isOnline() == false) {
            Toast.makeText(requireContext(), "No Network!!!", Toast.LENGTH_LONG).show()
            Log.d("Network", "No Network")
        }
        getFavoritedCities()
        getLiveData()
        binding.swipeToRefreshToday.setOnRefreshListener {
            Toast.makeText(requireContext(), "Refreshed!!!", Toast.LENGTH_SHORT).show()
            getLiveData()
            binding.swipeToRefreshToday.isRefreshing = false
        }
    }

    private fun getLiveData() {
        viewModel.unitData.observe(viewLifecycleOwner)
        { unitsLiveData ->
            val unit = unitsLiveData

            viewModel.weatherLiveData.observe(viewLifecycleOwner) { response ->
                binding.place.text = response?.name
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
                if (!binding.enterCity.text.isEmpty()) {
                    getToday(binding.enterCity.text.toString(), unit)
                } else {
                    Toast.makeText(requireContext(), "Enter City", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun getFavoritedCities() {
        viewModel.getFavoritedCities.observe(viewLifecycleOwner, {

            if (it.isNotEmpty()) {
                val allCities = mutableListOf<String>()
                for (i in it) {
                    allCities.add(i.name!!)
                }
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.dropdown_cities, allCities)
                binding.enterCity.setAdapter(arrayAdapter)
            }
        })
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
        if (binding.favorite.isChecked()) {
            binding.favorite.setChecked(false)
        }
        Log.d("getToday", "Success")
        viewModel.getCurrentWeather(city, unit)
        viewModel.weatherLiveData.observe(viewLifecycleOwner)
        { response ->
            if (response == null) {
                Log.d("Response", "Null")
                Toast.makeText(requireContext(), "No Response", Toast.LENGTH_SHORT).show()
            } else {
                val main = response?.main
                binding.place.text = response?.name
                var unitSymbol: String = getString(R.string.celsiusSymbol)
                if (unit == getString(R.string.metric)) {
                    unitSymbol = getString(R.string.celsiusSymbol)
                }
                if (unit == getString(R.string.imperial)) {
                    unitSymbol = getString(R.string.fahrenheitSymbol)
                }
                binding.temperature.text = main?.temp.toString() + unitSymbol
                binding.highTemperature.text = main?.tempMax.toString() + unitSymbol
                binding.lowTemperature.text = main?.tempMin.toString() + unitSymbol

//                val sdf = android.icu.text.SimpleDateFormat(
//                    getString(R.string.dateFormat),
//                    Locale.ENGLISH
//                )
//                val date = sdf.format(Date(response?.dt?.toLong()?.times(1000)!!))
                val sdf1 = SimpleDateFormat(getString(R.string.dateFormat1), Locale.getDefault())
                val date1 = sdf1.format(Date()).toString()


                binding.dateTime.text = date1

                val WeatherItem = response?.weather?.get(0)
                binding.iconText.text = WeatherItem?.description
                val iconId = WeatherItem?.icon
                Glide.with(this).load(IMAGE_URL.format("$iconId"))
                    .error(R.drawable.ic_baseline_cloud_circle_24)
                    .into(binding.icon)

                binding.favorite.setOnCheckedChangeListener { checkBox, isChecked ->
                    if (isChecked) {
                        if (response != null && !binding.place.text.isEmpty()) {
                            val coord = response?.coord
                            val cityInfo =
                                Cities(response?.id, response?.name, coord?.lon, coord?.lat)

                            viewModel.insert(cityInfo)

                            Toast.makeText(
                                requireContext(),
                                "${response?.name} is Favorited",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(requireContext(), "No Response", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    if (!isChecked) {
                        if (response != null && !binding.place.text.isEmpty()) {
                            response.id?.let { viewModel.delete(it) }
                            Toast.makeText(
                                requireContext(),
                                "${response?.name} is Unfavorited",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(requireContext(), "No Response", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }
            }
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        var state: Boolean
        state = capabilities != null
        return state
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
