package com.example.projekt1rain.Room

import android.util.Base64.encodeToString
import androidx.room.TypeConverter
import com.beust.klaxon.Json
import com.example.projekt1rain.Coord
import com.example.projekt1rain.CurrentWeather
import com.example.projekt1rain.OneCall
import com.google.gson.Gson


import com.example.projekt1rain.*
import com.example.projekt1rain.RetrofitApi.CurrentWeatherResponse
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class ObjectConverter {


    //TypeConverter for all Entitys  and Lists that are not from primitive DataTypes like String,Int
    // thats why we need that TypeConverter
    //ObjectConverter intitallize in WeatherDatabase class

    @TypeConverter
    fun fromCoord(coord: Coord): String {
        return Gson().toJson(coord)
    }

    @TypeConverter
    fun toCoord(coord: String): Coord {
        return Gson().fromJson(coord, Coord::class.java)
    }

    @TypeConverter
    fun fromWeather(weather: CurrentWeather): String {
        return Gson().toJson(weather)
    }

    @TypeConverter
    fun toWeather(weather: String): CurrentWeather {
        return Gson().fromJson(weather, CurrentWeather::class.java)
    }

    @TypeConverter
    fun fromList(daily: OneCall): String {
        return Gson().toJson(daily)
    }

    @TypeConverter
    fun toList(daily: String): OneCall {
        return Gson().fromJson(daily, OneCall::class.java)
    }

    @TypeConverter
    fun fromCloud(clouds: Clouds): String {
        return Gson().toJson(clouds)
    }

    @TypeConverter
    fun toCloud(clouds: String): Clouds? {
        return Gson().fromJson(clouds, Clouds::class.java)
    }

    @TypeConverter
    fun fromSys(sys: Sys): String {
        return Gson().toJson(sys)
    }

    @TypeConverter
    fun toSys(sys: String): Sys? {
        return Gson().fromJson(sys, Sys::class.java)
    }

    @TypeConverter
    fun fromMain(main: Main): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toMain(main: String): Main? {
        return Gson().fromJson(main, Main::class.java)
    }

    @TypeConverter
    fun fromWind(wind: Wind): String {
        return Gson().toJson(wind)
    }

    @TypeConverter
    fun toWind(wind: String): Wind? {
        return Gson().fromJson(wind, Wind::class.java)
    }

    @TypeConverter
    fun fromString(value: String?): List<Weather> {
        val listType: Type = object : TypeToken<List<Weather?>?>() {}.type
        return Gson().fromJson<List<Weather>>(value, listType)
    }

    @TypeConverter
    fun listToString(list: List<Weather?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromCity(city: City): String {
        return Gson().toJson(city)
    }

    @TypeConverter
    fun toCity(city: String): City {
        return Gson().fromJson(city, City::class.java)
    }


    @TypeConverter
    fun fromMain2(currentWeatherResponse: CurrentWeatherResponse): String {
        return Gson().toJson(currentWeatherResponse)
    }

    @TypeConverter
    fun toMain2(currentWeatherResponse: String): CurrentWeatherResponse? {
        return Gson().fromJson(currentWeatherResponse, CurrentWeatherResponse::class.java)
    }

    @TypeConverter
    fun fromListtoStringMinutely(value: String?): List<Minutely> {
        val listType: Type = object : TypeToken<List<Minutely?>?>() {}.type
        return Gson().fromJson<List<Minutely>>(value, listType)
    }

    @TypeConverter
    fun listtoStringMinutely(list: List<Minutely?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromListtoStringHourly(value: String?): List<Hourly> {
        val listType: Type = object : TypeToken<List<Hourly?>?>() {}.type
        return Gson().fromJson<List<Hourly>>(value, listType)
    }

    @TypeConverter
    fun listtoStringHourly(list: List<Hourly?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromListtoStringDaily(value: String?): List<Daily> {
        val listType: Type = object : TypeToken<List<Daily?>?>() {}.type
        return Gson().fromJson<List<Daily>>(value, listType)
    }

    @TypeConverter
    fun listtoStringDaily(list: List<Daily?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

}
