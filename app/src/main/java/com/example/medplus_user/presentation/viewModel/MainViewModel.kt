package com.example.medplus_user.presentation.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medplus_user.common.Resource
import com.example.medplus_user.data.location.AddressResolver
import com.example.medplus_user.data.repository.LocationProvider
import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.models.Medicines
import com.example.medplus_user.domain.models.Shopkeeper
import com.example.medplus_user.domain.useCases.GetAllMedicinesUseCase
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationProvider: LocationProvider,
    private val addressResolver: AddressResolver,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getAllMedicinesUseCase: GetAllMedicinesUseCase,
    private val getMedicinesByCategoryUseCase: GetMedicinesByCategoryUseCase,
    private val getMedicinesByNameUseCase: GetMedicineByNameUseCase,
    private val getMedicinesByIdUseCase: GetaMedicineByIdUseCase,
    private val shopkeeperUseCases: ShopkeeperUseCases
) : ViewModel() {
//    fetch categories...

    val categories: StateFlow<Resource<List<Category>>> = getCategoriesUseCase
        .invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )

    //    get all medicines
    private val _allMedicines = MutableStateFlow<Resource<List<Medicines>>>(Resource.Loading())
    val allMedicines: StateFlow<Resource<List<Medicines>>> = _allMedicines
    fun fetchAllMedicines() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _allMedicines.value = Resource.Loading()
                _allMedicines.value = getAllMedicinesUseCase.invoke()
            } catch (e: Exception) {
                _allMedicines.value = Resource.Error(e.message ?: "Something went wrong")
            }
        }
    }

    /* Searching*/
    private val _searchQueryMed = MutableStateFlow("")
    val searchQuery = _searchQueryMed.asStateFlow()

    val filteredMedicines = combine(_searchQueryMed, allMedicines) { query, result ->
        val list = result.data ?: emptyList()
        if (query.isBlank()) list
        else list.filter {
            it.ingredients?.contains(query, ignoreCase = true) ?: false || // i can other fields if needed
                    it.medicineName?.contains(query, ignoreCase = true) ?: false
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun updateQueryMed(query: String) {
        _searchQueryMed.value = query
    }

    //    fetch All-Shopkeepers
    private val _allShopkeepers = MutableStateFlow<Resource<List<Shopkeeper>>>(Resource.Loading())
    val allShopkeepers = _allShopkeepers.asStateFlow()
    fun fetchAllShopkeepers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _allShopkeepers.value = Resource.Loading()
                _allShopkeepers.value = shopkeeperUseCases.invoke()
            } catch (e: Exception) {
                _allShopkeepers.value = Resource.Error(e.message ?: "Something went wrong")
            }
        }
    }

    /* search shopkeepers*/
    private val _shopQuerySearch = MutableStateFlow("")
    val shopQuerySearch = _shopQuerySearch.asStateFlow()
    fun updateQueryShop(query: String) {
        _shopQuerySearch.value = query
    }

    val filteredShopkeepers = combine(_shopQuerySearch, allShopkeepers) { query, result ->
        val list = result.data ?: emptyList()
        if (query.isBlank()) list
        else list.filter {
            it.ownerName?.contains(query, ignoreCase = true) ?: false ||
                    it.address?.contains(query, ignoreCase = true) ?: false ||
                    it.shopName?.contains(query, ignoreCase = true) ?: false
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

/* fetch medicines by category , then use it to search in it */
    private val _categorySpecificMedicinesQuery = MutableStateFlow("")
    val categorySpecificMedicinesQuery = _categorySpecificMedicinesQuery.asStateFlow()

    private val _categorySpecificMedicines = MutableStateFlow<Resource<List<Medicines>>>(Resource.Loading())
    val categorySpecificMedicines = _categorySpecificMedicines.asStateFlow()

    fun updateCategorySpecificMedicinesQuery(query: String) {
        _categorySpecificMedicinesQuery.value = query
    }
    fun getMedicinesByCategory(catId: String) {
        viewModelScope.launch {
            try{
                _categorySpecificMedicines.value = Resource.Loading()
                _categorySpecificMedicines.value = getMedicinesByCategoryUseCase(catId)
            }
            catch (e : Exception){
                _categorySpecificMedicines.value = Resource.Error(e.message ?: "Something went wrong")
            }
        }
    }

    val filteredCategoryMedicines = combine(
        _categorySpecificMedicinesQuery,
        categorySpecificMedicines
    ) { query, result ->
        val list = result.data ?: emptyList()
        if (query.isBlank()) list
        else list.filter {
            it.medicineName?.contains(query, ignoreCase = true) ?: false
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

}