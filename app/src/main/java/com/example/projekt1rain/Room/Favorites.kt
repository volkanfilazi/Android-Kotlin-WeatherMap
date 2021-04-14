package com.example.projekt1rain.Room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projekt1rain.Coord
import com.example.projekt1rain.CurrentWeather
import com.example.projekt1rain.RetrofitApi.CurrentWeatherResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable
//Entity for the ForYou Page Favorites
@Entity(tableName = "Favorites")
data class Favorites  (
    @PrimaryKey(autoGenerate = false) var id:String,
    val address:String,
    val currentWeatherResponse:CurrentWeatherResponse?

):Serializable

