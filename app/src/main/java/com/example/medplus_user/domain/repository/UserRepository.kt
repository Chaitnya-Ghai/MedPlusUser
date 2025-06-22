package com.example.medplus_user.domain.repository

import com.example.medplus_user.common.Resource
import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.models.Medicines
import com.example.medplus_user.domain.models.Shopkeeper
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    // Fetches the list of categories from remote or local sources
    fun getCategories():Flow<Resource<List<Category>>>
    suspend fun getAllMedicines() : Resource<List<Medicines>>
    suspend fun getAllShopkeepers() : Resource<List<Shopkeeper>>
    suspend fun getMedicinesByCategory(catId : String) : Resource<List<Medicines>>
}