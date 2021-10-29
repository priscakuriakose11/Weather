package com.example.weather.data.Repository

import com.example.weather.data.api.OpenWeatherApiService
import com.example.weather.data.utils.CurrentWeatherResponse
import retrofit2.Response

class ApiClient(private val openWeatherApiService: OpenWeatherApiService) {
    suspend fun getCurrentWeather(city: String): Response<CurrentWeatherResponse> {
        return openWeatherApiService.getCurrentWeather(city)
    }
}