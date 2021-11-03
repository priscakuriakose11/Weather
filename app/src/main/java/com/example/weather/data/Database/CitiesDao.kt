package com.example.weather.data.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CitiesDao {
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insert(cities:Cities)
    @Query("SELECT name from Cities")
     fun display():LiveData<List<Cities>>

}