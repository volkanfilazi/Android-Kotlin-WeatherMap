package com.example.projekt1rain

import android.net.LinkAddress
import com.example.projekt1rain.CurrentWeather


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import com.google.gson.annotations.SerializedName
import java.util.function.DoubleBinaryOperator

private val klaxon = Klaxon()

@Entity
data class OneCall @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true) var onecallid: Long,
    val lat: Double,
    val lon: Double,
    val timezone: String,


    @SerializedName("timezone_offset")
    val timezoneOffset: Long,
    val weather: List<CurrentWeather>,
    val current: CurrentWeather,
    val daily: List<Daily>,
    val minutely: List<Minutely>,
    val hourly: List<Hourly>,
    val temp: Temp,

    ) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<OneCall>(json)
    }
}


data class Minutely(
    val dt: Long,
    val precipitation: Long
)

data class Hourly(
    val dt: Long,
    val precipitation: Long,
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_deg")
    val windDeg: Int,
)

data class Daily(

    val dt: Long,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    @SerializedName("wind_deg")
    val windDeg: Int,
    val pop: Float,
    val rain: Float,
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double,

    /*     fun getMetricMax(
     max = .toInt().minus(273.15.toInt().toString().toFloat())
)*/
)


