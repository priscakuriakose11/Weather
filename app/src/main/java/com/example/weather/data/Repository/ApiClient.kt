package com.example.weather.data.Repository

import com.example.weather.data.api.OpenWeatherApiService
import com.example.weather.data.model.ForecastWeatherResponse
import com.example.weather.data.utils.CurrentWeatherResponse
import retrofit2.Response

class ApiClient(private val openWeatherApiService: OpenWeatherApiService) {
    suspend fun getCurrentWeather(city: String): Response<CurrentWeatherResponse> {
        return openWeatherApiService.getCurrentWeather(city)
    }

    suspend fun getForecastWeather(lat: Double, lon: Double): Response<ForecastWeatherResponse> {
        return openWeatherApiService.getForecastWeather(lat, lon)
    }
}