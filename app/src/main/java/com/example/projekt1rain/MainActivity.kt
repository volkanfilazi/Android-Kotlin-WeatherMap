package com.example.projekt1rain

import android.Manifest
import android.Manifest.permission
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.projekt1rain.DataStorag.DataService
import com.example.projekt1rain.DataStorag.SharedPrefs
import com.example.projekt1rain.Fragments.ForYouFragment
import com.example.projekt1rain.Fragments.MapViewFragment
import com.example.projekt1rain.Fragments.SettingsFragment
import com.example.projekt1rain.InternetCheck.CheckNet
import com.example.projekt1rain.Room.City
import com.example.projekt1rain.Room.LocalJSONParser
import com.example.projekt1rain.Room.WeatherDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import java.util.concurrent.Executors


class MainActivity() : AppCompatActivity() {
    internal lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefs = SharedPrefs(context = this)
        if (sharedPrefs.loadDarkModeState() == true) {
            Log.i("theme", "@darktheme")
            setTheme(R.style.DarkTheme)
        } else {
            Log.i("theme", "@brighttheme")
            setTheme(R.style.BrightTheme)
        }
        setContentView(R.layout.activity_main)
        val dataService: DataService = (application as MyApp).dataService
        setToolbar()
        /// SAVE ASSETS ONLY ONE TIME
        if (!Utility.getBooleanPreferenceValue(this, "isFirstTimeExecution")) {
            val jsonFileString =
                LocalJSONParser.getJsonDataFromAsset(applicationContext, "citylist.json")
            val gson = Gson()
            //PARSE JSON TO STRING
            val listPersonType = object : TypeToken<List<City>>() {}.type
            val cities: List<City> = gson.fromJson(jsonFileString, listPersonType)
            Executors.newSingleThreadExecutor().execute { dataService.saveCities(cities) }
            informationAlert()
            showProgressBar()
            Log.d("tag", "First time Execution")
            Utility.setBooleanPreferenceValue(this, "isFirstTimeExecution", true)
        }
        //Bottom nav bar
        val nav = findViewById<BottomNavigationView>(R.id.nav_view)
        val foryoufragment = ForYouFragment()
        val mapViewFragment = MapViewFragment()
        val settingsFragment = SettingsFragment()
        makeCurrentFragment(foryoufragment)

        nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.forYouFragment -> makeCurrentFragment(foryoufragment)
                R.id.mapViewFragment -> makeCurrentFragment(mapViewFragment)
                R.id.settingsFragment -> makeCurrentFragment(settingsFragment)
            }
            true
        }

        val networkConnection = CheckNet(applicationContext)
        networkConnection.observe(this, androidx.lifecycle.Observer { isConnected ->

            if (isConnected) {
                Toast.makeText(this, getString(R.string.erfolgreich), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.keineverbindung), Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun informationAlert() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.infolayout, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        mBuilder.show()
    }

    private fun setToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {

        }

    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }

    private fun showProgressBar() {

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progressbar, null)
        val message = dialogView.findViewById<TextView>(R.id.text)
        message.text = getString(R.string.save)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()

        Handler().postDelayed({ dialog.dismiss() }, 50000)

    }
}





