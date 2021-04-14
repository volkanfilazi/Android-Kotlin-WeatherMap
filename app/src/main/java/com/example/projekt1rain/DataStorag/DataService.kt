package com.example.projekt1rain.DataStorag

import android.app.Application
import androidx.appcompat.widget.SearchView
import com.example.projekt1rain.*
import com.example.projekt1rain.InterFaces.CallBack
import com.example.projekt1rain.InterFaces.GetName
import com.example.projekt1rain.InterFaces.RemoveCallBack
import com.example.projekt1rain.Room.City
import com.example.projekt1rain.Room.Favorites
import javax.security.auth.callback.Callback

interface DataService {

    fun saveCities(cities: List<City>)

    fun getCitiesFindbyName(name: String, getName: GetName)

    fun saveFavorites(favorites: Favorites)

    fun getFavorites(callback: CallBack)

    fun deleteFavorites(favorites: Favorites, removeCallBack: RemoveCallBack)


}