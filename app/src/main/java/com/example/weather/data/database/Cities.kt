package com.example.weather.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Cities")
data class Cities(
    @PrimaryKey(autoGenerate = false) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String? = null,
    val lon: Double? = null,
    val lat: Double? = null
)
