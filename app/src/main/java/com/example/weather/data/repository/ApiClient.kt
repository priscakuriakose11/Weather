package com.example.weather.data.repository

import com.example.weather.data.api.OpenWeatherApiService
import com.example.weather.data.model.ForecastWeatherResponse
import com.example.weather.data.utils.CurrentWeatherResponse
import retrofit2.Response
import java.lang.Exception

class ApiClient(private val openWeatherApiService: OpenWeatherApiService) {
    suspend fun getCurrentWeather(
        city: String,
        unit: String
    ): SimpleResponse<CurrentWeatherResponse> {
        return safeApiCall { openWeatherApiService.getCurrentWeather(city, unit) }
    }


    suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        unit: String
    ): SimpleResponse<ForecastWeatherResponse> {
        return safeApiCall { openWeatherApiService.getForecastWeather(lat, lon, unit) }
    }


    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): SimpleResponse<T> {
        return try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            SimpleResponse.failure(e)
        }
    }
}