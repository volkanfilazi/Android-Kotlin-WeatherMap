package com.example.projekt1rain.Room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.example.projekt1rain.Coord
import com.example.projekt1rain.CurrentWeather
import com.google.gson.annotations.SerializedName

//Entity for CITYLIST.JSON ASSET
@Entity(tableName = "City")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
data class City(
    val name: String,
    val state: String,
    val country: String,
    val coord: Coord,
    @PrimaryKey(autoGenerate = true) var cityid: Long

)



