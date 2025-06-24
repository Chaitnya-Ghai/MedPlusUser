package com.example.medplus_user.common

import android.location.Location

class Constants {
    companion object{
        const val MEDICINE="medicines"
        const val CATEGORY="category"
        const val PHARMACIST = "Pharmacist"
    }
}
object LocationUtils {
    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Float {
        val result = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, result)
        return result[0] // distance in meters
    }
}
fun Float.formatKm(): String = String.format("%.1f km", this / 1000f)

