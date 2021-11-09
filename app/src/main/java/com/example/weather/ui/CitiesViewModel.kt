package com.example.weather.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.weather.data.database.Cities
import com.example.weather.data.database.CitiesDatabase
import com.example.weather.data.model.ForecastWeatherResponse
import com.example.weather.data.repository.CitiesRepository
import com.example.weather.data.repository.SettingsRepository
import com.example.weather.data.repository.WeatherRepository
import com.example.weather.data.utils.CurrentWeatherResponse
import kotlinx.coroutines.launch

class CitiesViewModel(application: Application) :AndroidViewModel(application){
    private val citiesRepository: CitiesRepository
    var readAll: LiveData<List<Cities>>
    init {
        val citiesDB= CitiesDatabase.getInstance(application).citiesDao()
        citiesRepository= CitiesRepository(citiesDB)
        readAll=citiesRepository.display()
    }
    fun addCity(city: Cities){
        viewModelScope.launch {
            citiesRepository.insert(city)
        }
    }

    private val repository = WeatherRepository()

    private val _weatherLiveData = MutableLiveData<CurrentWeatherResponse?>()
    val weatherLiveData: LiveData<CurrentWeatherResponse?> = _weatherLiveData

    fun getCurrentWeather(city: String,unit:String) {
        viewModelScope.launch {
            val response = repository.getCurrentWeather(city,unit)
            Log.d("ViewModel", "Success")
            _weatherLiveData.postValue(response)
        }
    }

    private val _weatherLiveData1 = MutableLiveData<ForecastWeatherResponse?>()
    val forecastWeatherLiveData: LiveData<ForecastWeatherResponse?> = _weatherLiveData1

    fun getForecastWeather(lat: Double, lon: Double ,unit: String) {
        viewModelScope.launch {
            val forecastResponse = repository.getForecastWeather(lat, lon,unit)
            Log.d("ViewModel", "Success")
            _weatherLiveData1.postValue(forecastResponse)
        }
    }


    private val settingsRepository = SettingsRepository(application)

    val unitData =settingsRepository.unitFlow.asLiveData()

    fun saveCurrentUnit( unit:String)=viewModelScope.launch {
        settingsRepository.saveCurrentUnit(unit)
    }
}