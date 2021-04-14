package com.example.projekt1rain.RetrofitApi

import androidx.room.Embedded
import com.example.projekt1rain.*
import com.example.projekt1rain.CurrentWeather.*
import com.google.gson.annotations.SerializedName

class CurrentWeatherResponse {
    var main: Main? = null
    var lat:Double? = null
    var lon:Double?= null
    var current:CurrentWeather? = null
    var hourly: List<Hourly>? = null
    var daily: List<Daily>? = null

}