package com.example.medplus_user.data.repository

import android.location.Location

interface LocationProvider {
    suspend fun getCurrentLocation(): Location?
}