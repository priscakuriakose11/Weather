package com.example.weather.data.repository

import com.example.weather.data.api.OpenWeatherApiService
import com.example.weather.data.model.ForecastWeatherResponse
import com.example.weather.data.utils.CurrentWeatherResponse
import retrofit2.Response

class ApiClient(private val openWeatherApiService: OpenWeatherApiService) {
    suspend fun getCurrentWeather(city: String,unit:String): Response<CurrentWeatherResponse> {
        return openWeatherApiService.getCurrentWeather(city,unit)
    }

    suspend fun getForecastWeather(lat: Double, lon: Double,unit:String): Response<ForecastWeatherResponse> {
        return openWeatherApiService.getForecastWeather(lat, lon,unit)
    }
}