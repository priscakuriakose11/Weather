package com.example.weather.data.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName="Cities")
data class Cities(
    @PrimaryKey(autoGenerate = false) val id: Int? = null,
    @ColumnInfo(name = "NAME") val name: String? = null,
    val lon: Double? = null,
    val lat: Double? = null
)
