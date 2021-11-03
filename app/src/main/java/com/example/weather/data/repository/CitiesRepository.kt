package com.example.weather.data.repository

import androidx.lifecycle.LiveData
import com.example.weather.data.Database.Cities
import com.example.weather.data.Database.CitiesDao

class CitiesRepository(private val citiesDao:CitiesDao) {
    suspend fun insert(cities:Cities)=citiesDao.insert(cities)

    fun display(): LiveData<List<Cities>> =citiesDao.display()
}