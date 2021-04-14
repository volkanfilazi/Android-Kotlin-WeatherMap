package com.example.projekt1rain.Room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object DatabaseProvider {
    private var db: WeatherDatabase? = null

    fun getInstance(): WeatherDatabase {
        if (db == null) throw RuntimeException("Database not initialized")
        return db!!
    }

    fun initDatabase(context: Context) {
        db = Room.databaseBuilder(context, WeatherDatabase::class.java, "User-Database").build()
    }
}