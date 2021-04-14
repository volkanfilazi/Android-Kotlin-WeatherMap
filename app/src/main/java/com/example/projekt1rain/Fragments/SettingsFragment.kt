package com.example.projekt1rain.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.projekt1rain.MainActivity
import com.example.projekt1rain.R


class SettingsFragment() : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.settingspreferences, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreference("DARK_MODE")?.setOnPreferenceChangeListener { preference, newvalue ->
            if (preference.key == "DARK_MODE") {
                //restartApp()
                Toast.makeText(requireContext(), (getString(R.string.neustart)), Toast.LENGTH_LONG)
                    .show()
            }
            Log.i("settings", "$preference")
            true

        }
    }


    fun restartApp() {
        val restart = Intent(requireActivity().applicationContext, MainActivity::class.java)
        startActivity(restart)
        requireActivity().finish()
    }
}


