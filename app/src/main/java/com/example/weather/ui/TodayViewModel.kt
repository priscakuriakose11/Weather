package com.example.weather.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.Repository.WeatherRepository
import com.example.weather.data.utils.CurrentWeatherResponse
import kotlinx.coroutines.launch

class TodayViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _weatherLiveData = MutableLiveData<CurrentWeatherResponse?>()
    val weatherLiveData: LiveData<CurrentWeatherResponse?> = _weatherLiveData

    fun getCurrentWeather(city: String) {
        viewModelScope.launch {
            val response = repository.getCurrentWeather(city)
            Log.d("VM", "ViewModel")
            _weatherLiveData.postValue(response)
        }
    }
}