package com.example.weather.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.data.database.Cities
import com.example.weather.data.database.CitiesDatabase
import com.example.weather.data.model.ForecastWeatherResponse
import com.example.weather.data.repository.CitiesRepository
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

    private val _weatherLiveData1 = MutableLiveData<ForecastWeatherResponse?>()
    val forecastWeatherLiveData: LiveData<ForecastWeatherResponse?> = _weatherLiveData1

    private var _unitsLivedata = MutableLiveData<String>("Metric")
    private var _unitsPositionLiveData=MutableLiveData<Int>(0)

    val unitsLiveData:LiveData<String> =_unitsLivedata
    val unitsPositionLiveData:LiveData<Int> = _unitsPositionLiveData



    fun getCurrentWeather(city: String,unit:String) {
        viewModelScope.launch {
            val response = repository.getCurrentWeather(city,unit)
            Log.d("ViewModel", "Success")
            _weatherLiveData.postValue(response)
        }
    }

    fun getForecastWeather(lat: Double, lon: Double ,unit: String) {
        viewModelScope.launch {
            val forecastResponse = repository.getForecastWeather(lat, lon,unit)
            Log.d("ViewModel", "Success")
            _weatherLiveData1.postValue(forecastResponse)
        }
    }

    fun getunit(newUnit:String,newUnitPosition:Int){
        _unitsLivedata.value=newUnit
        _unitsPositionLiveData.value=newUnitPosition
    }
}