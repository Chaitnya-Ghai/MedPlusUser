package com.example.medplus_user.data.location

import android.content.Context
import android.location.Geocoder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddressResolver@Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun getAddress(latitude: Double?, longitude: Double?): String = withContext(Dispatchers.IO) {
        try {
            if (latitude!=null && longitude!=null) {
                val addresses = Geocoder(context).getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) { return@withContext addresses[0].getAddressLine(0) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext "NO ADDRESS Found"
    }
}