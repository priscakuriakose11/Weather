package com.example.weather.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.data.Database.Cities
import com.example.weather.data.Database.CitiesDatabase
import com.example.weather.data.repository.CitiesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CitiesViewModel(application: Application) :AndroidViewModel(application){
    private val citiesRepository: CitiesRepository
    private var readAll: LiveData<List<Cities>>
    init {
        val citiesDB= CitiesDatabase.getInstance(application).citiesDao()
        citiesRepository= CitiesRepository(citiesDB)
        readAll=citiesRepository.display()
    }
    fun addCity(city: Cities){
        viewModelScope.launch (Dispatchers.Default){
            citiesRepository.insert(city)
        }
    }
}