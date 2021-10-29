package com.example.weather.data.api

import com.example.weather.data.utils.CurrentWeatherResponse
import com.example.weathersampleapp.data.utils.Constants
import com.example.weathersampleapp.data.utils.Constants.Companion.ApiKey
import com.example.weathersampleapp.data.utils.Constants.Companion.units
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


//https://api.openweathermap.org/data/2.5/weather?q=kochi&appid=68aa7fa3c08c62c9da18224556e7d3eb
interface OpenWeatherApiService {
    @GET("data/2.5/weather?appid=$ApiKey&units=$units")
    suspend fun getCurrentWeather(
        @Query("q") city: String
    ): Response<CurrentWeatherResponse>


}
