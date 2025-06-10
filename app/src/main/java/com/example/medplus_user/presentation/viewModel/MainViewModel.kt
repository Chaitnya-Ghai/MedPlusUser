package com.example.medplus_user.presentation.viewModel


import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medplus_user.data.location.AddressResolver
import com.example.medplus_user.data.repository.LocationProvider
import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.models.Medicines
import com.example.medplus_user.domain.models.Shopkeeper
import com.example.medplus_user.domain.useCases.GetCategoriesUseCase
import com.example.medplus_user.domain.useCases.GetMedicineByNameUseCase
import com.example.medplus_user.domain.useCases.GetMedicinesByCategoryUseCase
import com.example.medplus_user.domain.useCases.GetaMedicineByIdUseCase
import com.example.medplus_user.domain.useCases.ShopkeeperUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val getMedicinesUseCase: GetCategoriesUseCase,
    private val getMedicinesByCategoryUseCase: GetMedicinesByCategoryUseCase,
    private val getMedicinesByNameUseCase: GetMedicineByNameUseCase,
    private val getMedicinesByIdUseCase: GetaMedicineByIdUseCase,
    private val shopkeeperUseCases: ShopkeeperUseCases
) : ViewModel() {
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
    fun getMedicinesById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getMedicinesByIdUseCase(medId=id)
        }
    }
    private val _medicinesByCat = MutableStateFlow<List<Medicines>>(emptyList())
    val medicinesByCat: StateFlow<List<Medicines>> = _medicinesByCat

    fun getMedicinesByCategory(catId: String) {
        viewModelScope.launch {
            val result = getMedicinesByCategoryUseCase(catId) // this is just a suspend fun
            _medicinesByCat.value = result
        }
    }

    private val _shopkeepers = MutableStateFlow<List<Shopkeeper>>(emptyList())
    val shopkeepers: StateFlow<List<Shopkeeper>> = _shopkeepers.asStateFlow()

    fun fetchShopkeepers(medicineId: String) {
        viewModelScope.launch {
            shopkeeperUseCases(medicineId).collect { list ->
                _shopkeepers.value = list
            }
        }
    }
    fun getMedicinesByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getMedicinesByNameUseCase(name=name)
        }
    }
}