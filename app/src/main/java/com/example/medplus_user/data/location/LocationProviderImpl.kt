package com.example.medplus_user.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.medplus_user.data.repository.LocationProvider
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationProvider{
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    override suspend fun getCurrentLocation(): Location? {
        if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("LocationProvider", "Permission not granted")
            return null
        }
        return try {
            val location = fusedLocationClient.lastLocation.await()
            if (location != null) {
                Log.d("LocationProvider", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
            } else {
                Log.e("LocationProvider", "Location is null")
            }
            location
        } catch (e: Exception) {
            Log.e("LocationProvider", "Failed to get location: ${e.message}")
            null
        }
    }
}