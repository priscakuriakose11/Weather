package com.example.weather.data.api

import com.example.weather.data.Repository.ApiClient
import com.example.weathersampleapp.data.utils.Constants.Companion.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val openWeatherApiService: OpenWeatherApiService by lazy {
        retrofit.create(OpenWeatherApiService::class.java)
    }
    val apiClient = ApiClient(openWeatherApiService)
}
