package com.example.weather.data.model

data class ForecastDailyModel(
    var icon: String ?= null,
    var min: String? = null,
    var max: String? = null,
    var dt: String? = null
)
