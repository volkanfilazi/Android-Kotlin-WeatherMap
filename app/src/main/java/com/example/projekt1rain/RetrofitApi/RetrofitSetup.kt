package com.example.projekt1rain.RetrofitApi

import android.util.Log
import com.example.projekt1rain.Room.DatabaseProvider
import com.example.projekt1rain.Room.Favorites
import com.example.projekt1rain.Room.WeatherDatabase
import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import java.util.function.DoubleBinaryOperator

private const val TAG = "Retrofit Connection"
private lateinit var favorites:Favorites


object RetrofitSetup {
    //var prereferences = MapViewFragment.address
    var urlAll = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}"
    var url = "https://api.openweathermap.org/data/2.5/"
    val apiKey = "d459f98ffa705ad3f6c5e02f86d9fab9"
}

fun retrofitResponse(address: String, dataBase: WeatherDatabase = DatabaseProvider.getInstance()){

    val retrofit = Retrofit.Builder()
        .baseUrl(RetrofitSetup.url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val weatherApi = retrofit.create(CallWeatherApi::class.java)
    val weatherResponseCall = weatherApi.getWeather(address,RetrofitSetup.apiKey)

        weatherResponseCall!!.enqueue(object : Callback<CurrentWeatherResponse?> {
        override fun onResponse(call: Call<CurrentWeatherResponse?>, response: Response<CurrentWeatherResponse?>
        ) {
            if (response.code() == 200) {
                Log.d(TAG,"Successfuly")
            } else if (!response.isSuccessful) {
                Log.d(TAG,"Error")
            }
        }
        override fun onFailure(call: Call<CurrentWeatherResponse?>, t: Throwable) {
            Log.d(TAG,"Error im log.de${t.toString()}")

        }
    })
}
//get the OneCall and
fun retrofitOneCallResponse(lat:Double, long:Double, address: String, dataBase: WeatherDatabase = DatabaseProvider.getInstance()){
    val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }).build()
    val retrofit = Retrofit.Builder()
        .baseUrl(RetrofitSetup.url)
        .addConverterFactory(GsonConverterFactory.create())
            .client(client)
        .build()
    val weatherApi = retrofit.create(CallWeatherApi::class.java)

    val weatherOneCallResponse =weatherApi.getHourlyForecast(lat,long,RetrofitSetup.apiKey)
    weatherOneCallResponse!!.enqueue(object :Callback<CurrentWeatherResponse?>{
        override fun onResponse(
            call: Call<CurrentWeatherResponse?>,
            response: Response<CurrentWeatherResponse?>
        ) {
            if (response.code() == 200) {
                Log.d(TAG,"Successfuly")
            } else if (!response.isSuccessful) {
                Log.d(TAG,"Error")
            }
            val myOneCallData = response.body()
            val hourly = myOneCallData!!.hourly
            val hourlyTemperature = hourly?.get(2)?.temp
            val tempp =(hourlyTemperature!! - 273.15).toInt()
            val hourlyClouds = hourly?.get(4)?.clouds
            val hourlyPressure = hourly?.get(3)?.pressure

            Log.d("TAG","hourlyClouds : " + hourlyClouds)
            Log.d("TAG","hourlyPressure : " + hourlyPressure)
            Log.d("TAG","temppppp : " + tempp)

            //save the response directly in Room
            Executors.newSingleThreadExecutor().execute {
                val body = response.body()
                val favorites = Favorites("${body?.lat}${body?.lon}" ,address,body)
                dataBase.currentWeatherDao().insertfavorites(favorites)
            }

        }

        override fun onFailure(call: Call<CurrentWeatherResponse?>, t: Throwable) {
            Log.d(TAG,"Error im log.de${t.toString()}")
        }

    })
}
//METHOD FOR SWIPETOREFRESH WITHOUT INSERT IT IN THE DATABASE
fun retrofitOneCallrefreshResponse(lat:Double, long:Double, address: String, dataBase: WeatherDatabase = DatabaseProvider.getInstance()){
    val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }).build()
    val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitSetup.url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    val weatherApi = retrofit.create(CallWeatherApi::class.java)

    val weatherOneCallResponse =weatherApi.getHourlyForecast(lat,long,RetrofitSetup.apiKey)
    weatherOneCallResponse!!.enqueue(object :Callback<CurrentWeatherResponse?>{
        override fun onResponse(
                call: Call<CurrentWeatherResponse?>,
                response: Response<CurrentWeatherResponse?>
        ) {
            if (response.code() == 200) {
                Log.d(TAG,"Successfuly")
            } else if (!response.isSuccessful) {
                Log.d(TAG,"Error")
            }
            val myOneCallData = response.body()
            val hourly = myOneCallData!!.hourly
            val hourlyTemperature = hourly?.get(2)?.temp
            val tempp =(hourlyTemperature!! - 273.15).toInt()
            val hourlyClouds = hourly?.get(4)?.clouds
            val hourlyPressure = hourly?.get(3)?.pressure

            Log.d("TAG","hourlyClouds : " + hourlyClouds)
            Log.d("TAG","hourlyPressure : " + hourlyPressure)
            Log.d("TAG","temppppp : " + tempp)




        }

        override fun onFailure(call: Call<CurrentWeatherResponse?>, t: Throwable) {
            Log.d(TAG,"Error im log.de${t.toString()}")
        }

    })
}





