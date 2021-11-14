package com.example.weather.data.repository

import androidx.lifecycle.LiveData
import com.example.weather.data.database.Cities
import com.example.weather.data.database.CitiesDao

class CitiesRepository(private val citiesDao:CitiesDao) {
    suspend fun insert(cities:Cities)=citiesDao.insert(cities)

    fun displayCities(): LiveData<List<Cities>> =citiesDao.displayCities()

    fun delete(id:Int)=citiesDao.delete(id)
}