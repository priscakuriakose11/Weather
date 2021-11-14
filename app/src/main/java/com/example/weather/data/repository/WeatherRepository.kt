package com.example.weather.data.repository

import android.util.Log
import android.widget.Toast
import com.example.weather.data.api.RetrofitInstance
import com.example.weather.data.model.ForecastWeatherResponse
import com.example.weather.data.utils.CurrentWeatherResponse

class WeatherRepository {
    suspend fun getCurrentWeather(city: String, unit: String): CurrentWeatherResponse? {
        val request = RetrofitInstance.apiClient.getCurrentWeather(city, unit)
        if (request.failed) {
            Log.d("Network", "Request Failed")
            return null
        }
        if (!request.isSuccessful) {
            Log.d("Network", "Request Failed")
            return null
        }
        return request.body
    }

    suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        unit: String
    ): ForecastWeatherResponse? {
        val request = RetrofitInstance.apiClient.getForecastWeather(lat, lon, unit)
        if (request.failed) {
            Log.d("Network", "Request Failed")
            return null
        }
        if (!request.isSuccessful) {
            Log.d("Network", "Request Failed")
            return null
        }
        return request.body
    }


}
