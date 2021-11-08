package com.example.weather.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Cities::class],version = 1)
abstract class CitiesDatabase:RoomDatabase() {
    abstract fun citiesDao():CitiesDao

    companion object{
        var INSTANCE:CitiesDatabase?=null

        fun getInstance(context: Context):CitiesDatabase{
            if (INSTANCE==null){
              INSTANCE=Room.databaseBuilder(
                  context.applicationContext,
                  CitiesDatabase::class.java,
                  "Cities.db").build()

            }
            return INSTANCE!!
        }
    }

}