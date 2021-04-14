package com.example.projekt1rain.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.projekt1rain.CurrentWeather


@Dao
interface WeatherDao{

    @Insert ( onConflict = OnConflictStrategy.IGNORE)
     fun insert (city: City)

    @Insert ( onConflict = OnConflictStrategy.IGNORE)
    fun insertList (cities:List<City>)

    @Delete
     fun delete (city: City)

    @Query ("SELECT * FROM City WHERE name = :name LIMIT 1")
    fun getCityByName(name:String):City?

    @Query("SELECT * FROM City")
    fun  getLiveDataCityList():LiveData<List<City>>

    @Query("SELECT * FROM City")
    fun  getDataCityList():List<City>

    //favorites

    @Insert
    (onConflict = OnConflictStrategy.REPLACE)
     fun insertfavorites (favorites: Favorites)

    @Delete
     fun delete (favorites: Favorites)

    @Query ("SELECT * FROM Favorites ORDER BY address ASC ")
    fun getFavoritesList():List<Favorites>

}