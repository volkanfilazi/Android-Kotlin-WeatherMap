package com.example.projekt1rain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.beust.klaxon.Klaxon
import com.google.gson.annotations.SerializedName

private val klaxon = Klaxon()


@Entity
data class CurrentWeather @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = false) var id: Long,
    val base: String?,
    val clouds: Int?,
    val cod: Int?,
    val coord: Coord?,
    val dt: Int?,
    val main: Main?,
    val name: String?,
    val sys: Sys?,
    val timezone: Int?,
    val temp: Double?,
    val visibility: Int?,
    val weather: List<Weather>?,
    @SerializedName("wind_deg")
    val windDeg: Int?,
    val uvi: Double?,
    @SerializedName("wind_speed")
    val windSpeed: Double?,
    val wind: Wind?
)


data class Clouds(
    val all: Int?
)

data class Coord(
    val lat: Double?,
    val lon: Double?
)

data class Main(
    @SerializedName("feels_like")
    val feelsLike: Double?,
    val humidity: Int?,
    val pressure: Int?,
    val temp: Double?,
    @SerializedName("temp_max")
    val tempMax: Double?,
    @SerializedName("temp_min")
    val tempMin: Double?,
)

data class Sys(
    val country: String?,
    val id: Int?,
    val message: Double?,
    val sunrise: Int?,
    val sunset: Int?,
    val type: Int?,
)

data class Weather(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val main: String?
)

data class Wind(
    val deg: Int?,
    val speed: Double?
)

