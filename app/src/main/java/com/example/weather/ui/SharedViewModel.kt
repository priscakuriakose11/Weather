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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) :AndroidViewModel(application){
    private val citiesRepository: CitiesRepository
    var getFavoritedCities: LiveData<List<Cities>>
    init {
        val citiesDB= CitiesDatabase.getInstance(application).citiesDao()
        citiesRepository= CitiesRepository(citiesDB)
        getFavoritedCities=citiesRepository.displayCities()
    }
    fun insert(city: Cities){
        viewModelScope.launch (Dispatchers.IO){
            citiesRepository.insert(city)
        }
    }

    fun delete(id: Int){
        viewModelScope.launch (Dispatchers.IO){
            citiesRepository.delete(id)
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

    fun reset() {
        _weatherLiveData1.value = null
    }

    fun getForecastWeather(lat: Double, lon: Double ,unit: String) {
        viewModelScope.launch {
            val forecastResponse = repository.getForecastWeather(lat, lon,unit)
            Log.d("ViewModel", "Success")
            _weatherLiveData1.value= forecastResponse
        }
    }


    private val settingsRepository = SettingsRepository(application)

    val unitData =settingsRepository.unitFlow.asLiveData()

    fun saveCurrentUnit( unit:String)=viewModelScope.launch (Dispatchers.IO) {
        settingsRepository.saveCurrentUnit(unit)
    }
}