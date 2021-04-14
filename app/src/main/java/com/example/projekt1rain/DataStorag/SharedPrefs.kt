package com.example.projekt1rain.DataStorag

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.projekt1rain.InterFaces.GetName
import com.example.projekt1rain.Room.City
import com.example.projekt1rain.Room.DatabaseProvider
import com.example.projekt1rain.Room.TaskRunner
import com.example.projekt1rain.Room.WeatherDatabase

class SharedPrefs(context: Context) {
internal lateinit var sharedPrefsDark: SharedPreferences
init {

    sharedPrefsDark =  PreferenceManager.getDefaultSharedPreferences(context)
}
    fun setDarkModeState(state:Boolean?){
        val editor: SharedPreferences.Editor = sharedPrefsDark.edit()
        editor.putBoolean("DARK_MODE", state!!)
        editor.commit()
    }
    fun loadDarkModeState():Boolean? {
        return sharedPrefsDark.getBoolean("DARK_MODE", false)
    }

}
   /*
    fun getUnit() {
val sharedPreference: SharedPrefs = get
return
    }
}
*/

