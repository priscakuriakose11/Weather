package com.example.weather.data.Repository

import android.util.Log
import com.example.weather.data.api.RetrofitInstance
import com.example.weather.data.model.ForecastWeatherResponse
import com.example.weather.data.utils.CurrentWeatherResponse

class WeatherRepository {
    suspend fun getCurrentWeather(city: String): CurrentWeatherResponse? {
        val request = RetrofitInstance.apiClient.getCurrentWeather(city)
        if (request.isSuccessful) {
            Log.d("Repository", "Request Successful")
            return request.body()!!
        }
        Log.d("Repository", "Request Failed")
        return null
    }

    suspend fun getForecastWeather(lat: Double, lon: Double): ForecastWeatherResponse? {
        val request = RetrofitInstance.apiClient.getForecastWeather(lat, lon)
        if (request.isSuccessful) {
            Log.d("Repository", "Request Successful")
            return request.body()!!
        }
        Log.d("Repository", "Request Failed")
        return null
    }
}
