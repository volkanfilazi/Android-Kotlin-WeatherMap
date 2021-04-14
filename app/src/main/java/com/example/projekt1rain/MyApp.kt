package com.example.projekt1rain

import android.app.Application
import com.example.projekt1rain.DataStorag.DataClass
import com.example.projekt1rain.DataStorag.DataService
import com.example.projekt1rain.DataStorag.SharedPrefs
import com.example.projekt1rain.Room.DatabaseProvider
import com.example.projekt1rain.Room.WeatherDatabase


class MyApp() : Application() {
    lateinit var dataService: DataService
    lateinit var sharedPrefs: SharedPrefs
    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.initDatabase(this)
        dataService = DataClass()
        sharedPrefs = SharedPrefs(this)
        if (sharedPrefs.loadDarkModeState() == true) {

        }
    }
}
