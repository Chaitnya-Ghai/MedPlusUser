package com.example.medplus_user.domain.repository

import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.models.Medicines
import com.example.medplus_user.domain.models.Orders
import com.example.medplus_user.domain.models.Shopkeeper
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    // Fetches the list of categories from remote or local sources
    fun getCategories(): Flow<List<Category>>

    // Fetches the list of medicines from a specific category
    suspend fun getMedicinesByCategory(categoryId: String): List<Medicines>

    // Fetches the list of medicines from remote or local sources
    suspend fun getMedicines(): Flow<List<Medicines>>

    // Fetches the list of orders placed by the user
    suspend fun getOrders(): Flow<List<Orders>>

    // Places a new order for a user
    suspend fun placeOrder(order: Orders): Flow<Boolean>

    // Fetches the list of shopkeepers from remote or local sources
    suspend fun getShopkeepers(): Flow<List<Shopkeeper>>

}