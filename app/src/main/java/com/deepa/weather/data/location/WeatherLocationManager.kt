package com.deepa.weather.data.location

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.deepa.weather.models.Coord
import javax.inject.Inject


class WeatherLocationManager @Inject constructor(private val context: Context){
    fun isLocationPermissionGranted(): Boolean{
        return  ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED
    }

    fun getCurrentLocation(): Coord? {
        if( ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) return null
        try {
            val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
                return null
            } else {
                if (isNetworkEnabled) {
                    if (locationManager != null) {
                        val location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
//                            return Coord(37.2892356, -122.031308)
                            return Coord(location.latitude, location.longitude)
                        }
                    }
                } else
                    if (isGPSEnabled) {
                            if (locationManager != null) {
                                val location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                if (location != null) {
                                    return Coord(location.latitude, location.longitude)
                                }
                            }
                        }
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}