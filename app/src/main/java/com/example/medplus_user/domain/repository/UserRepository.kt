package com.example.medplus_user.domain.repository

import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.models.Medicines
import com.example.medplus_user.domain.models.Orders
import com.example.medplus_user.domain.models.Shopkeeper
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    // Fetches the list of categories from remote or local sources
    fun getCategories(): Flow<List<Category>>
    // Fetches the list of medicines from a specific
    suspend fun getMedicinesByCategory(categoryId: String): List<Medicines>
    suspend fun getMedicinesByName(name: String): List<Medicines>
    suspend fun getMedicine(medId: String): List<Medicines>
    // Fetches the list of orders
    suspend fun getOrders(): Flow<List<Orders>>
    suspend fun placeOrder(order: Orders): Flow<Boolean>
    // Fetches the list of shopkeepers from remote or local sources
    fun getShopkeepers(medicineId: String): Flow<List<Shopkeeper>>
}