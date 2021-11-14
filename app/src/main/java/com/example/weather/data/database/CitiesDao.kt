package com.example.weather.data.database

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
     fun displayCities():LiveData<List<Cities>>

    @Query("DELETE  from Cities WHERE id=:id")
     fun delete(id: Int)
}