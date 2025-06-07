package com.example.medplus_user.presentation.viewModel


import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medplus_user.data.location.AddressResolver
import com.example.medplus_user.data.repository.LocationProvider
import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.useCases.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationProvider: LocationProvider,
    private val addressResolver: AddressResolver,
    private val getMedicinesUseCase: GetCategoriesUseCase
) : ViewModel() {
//    private val _loading = MutableLiveData<Boolean>()
//    val loading: LiveData<Boolean> = _loading
    val categories :StateFlow<List<Category>> = getMedicinesUseCase
        .invoke()
        .stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000L),initialValue = emptyList())


    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    private val _address = MutableStateFlow<String?>(null)
    val address = _address.asStateFlow()

    fun fetchLocation(){
        viewModelScope.launch {
            _location.value = locationProvider.getCurrentLocation()
        }
    }

    fun getAddressLine(latitude: Double? = location.value?.latitude, longitude: Double?= location.value?.longitude){
        viewModelScope.launch {
            _address.value = addressResolver.getAddress(latitude=latitude,longitude=longitude)
        }
    }
}