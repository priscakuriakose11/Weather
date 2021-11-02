package com.example.weather.data.api

import com.example.weather.data.model.ForecastWeatherResponse
import com.example.weather.data.utils.CurrentWeatherResponse
import com.example.weathersampleapp.data.utils.Constants
import com.example.weathersampleapp.data.utils.Constants.Companion.ApiKey
import com.example.weathersampleapp.data.utils.Constants.Companion.units
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface OpenWeatherApiService {
    //https://api.openweathermap.org/data/2.5/weather?q=kochi&appid=68aa7fa3c08c62c9da18224556e7d3eb
    @GET("data/2.5/weather?appid=$ApiKey")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") unit:String
    ): Response<CurrentWeatherResponse>

    //https://api.openweathermap.org/data/2.5/onecall?lat=9.9399&lon=76.2602&exclude=minutely,hourly&appid=68aa7fa3c08c62c9da18224556e7d3eb
    @GET("data/2.5/onecall?exclude=minutely,hourly&appid=$ApiKey")
    suspend fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit:String
    ): Response<ForecastWeatherResponse>

}
