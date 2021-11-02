package com.example.weather.data.Database

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface CitiesDao {
    @Insert
    suspend fun insert(cities:Cities)
}