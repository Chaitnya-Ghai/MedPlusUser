package com.example.medplus_user.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkChecker(private val context: Context) {
    // A StateFlow to emit the network status
    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable: StateFlow<Boolean> get() = _isNetworkAvailable

    init {
        // Set initial network state based on the current connectivity
        _isNetworkAvailable.value = isNetworkAvailable()

        // Listen for changes in network connectivity
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Network is available, update the flow to true
                _isNetworkAvailable.value = true
            }

            override fun onLost(network: Network) {
                // Network is lost, update the flow to false
                _isNetworkAvailable.value = false
            }
        })
    }

    // Function to check if the device is connected to the internet
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        networkCapabilities?.let {
            return it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
        return false
    }
}