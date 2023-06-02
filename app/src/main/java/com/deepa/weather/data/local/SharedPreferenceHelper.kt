package com.deepa.weather.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.reflect.Type
import javax.inject.Inject


class SharedPreferenceHelper @Inject constructor(private val appContext: Context) {
    companion object {
        const val sharedPrefName: String = "weatherPref"
        const val locationPermissionFlowShown: String = "location_perm_shown"
    }

    fun isLocationPermissionDialogShown(): Boolean {
        val weatherPref: SharedPreferences =
            appContext.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        return weatherPref.getBoolean(locationPermissionFlowShown, false)
    }

    fun onLocationPermissionDialogShown() {
        val weatherPref: SharedPreferences =
            appContext.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = weatherPref.edit()
        prefsEditor.putBoolean(locationPermissionFlowShown, true)
        prefsEditor.commit()
    }


    fun <T> saveData(key: String, data: T?) {
        val weatherPref: SharedPreferences =
            appContext.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = weatherPref.edit()
        val gson = Gson()
        val jsonData = gson.toJson(data)
        prefsEditor.putString(key, jsonData)
        prefsEditor.commit()
    }

    fun <T> getData(key: String, type: Type): T? {
        val weatherPref: SharedPreferences =
            appContext.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val gson = Gson()
        val jsonData = weatherPref.getString(key, "")
        return gson.fromJson(jsonData, type)
    }
}