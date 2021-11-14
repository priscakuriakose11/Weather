package com.example.weather.data.model

import androidx.room.Update

data class ForecastDailyModel(
    var icon: String ?= null,
    var min: String? = null,
    var max: String? = null,
    var dt: String? = null,
    var update: String? =null
)
