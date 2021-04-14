package com.example.projekt1rain.RetrofitApi

import com.example.projekt1rain.Hourly
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query

interface CallWeatherApi {

    @GET("weather")
    fun getWeather(@Query("q") cityname: String?,
                   @Query("appid") apiKey: String?
    ):Call<CurrentWeatherResponse?>?

    @GET("onecall")
    fun getHourlyForecast(@Query("lat") lat: Double?,
                         @Query("lon") long: Double?,
               /*          @retrofit2.http.Query("units") unit:Int,*/
                         @Query("appid") apiKey: String?,
    ):Call<CurrentWeatherResponse?>?



    


}


