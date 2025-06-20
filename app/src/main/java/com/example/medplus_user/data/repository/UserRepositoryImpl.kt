package com.example.medplus_user.data.repository

import android.util.Log
import com.example.medplus_user.common.NetworkChecker
import com.example.medplus_user.data.local.dao.CategoryDao
import com.example.medplus_user.data.local.dao.MedicinesDao
import com.example.medplus_user.data.mappers.toDomain
import com.example.medplus_user.data.mappers.toEntity
import com.example.medplus_user.data.remote.FirebaseService
import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.models.Medicines
import com.example.medplus_user.domain.models.Orders
import com.example.medplus_user.domain.models.Shopkeeper
import com.example.medplus_user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val networkChecker: NetworkChecker,
    private val firebaseService: FirebaseService,
    private val categoryDao: CategoryDao,
    private val medicinesDao: MedicinesDao
): UserRepository {
    override fun getCategories(): Flow<List<Category>> = flow {
        // 1. Emit local data immediately
        val localEntities = categoryDao.showCategories()
        val localData = localEntities.map { it.toDomain() }
        Log.d("UserRepository", "Emitting local data first: $localData")
        emit(localData)

        // 2. Try to fetch from Firebase if network is available
        if (networkChecker.isNetworkAvailable()) {
            try {
                val dtoList = firebaseService.getCategories()
                Log.d("UserRepository", "Fetched DTOs: $dtoList")

                val categories = dtoList.map { it.toDomain() }
                Log.d("UserRepository", "Mapped to Domain: $categories")

                val entities = categories.map { it.toEntity() }
                val validEntities = entities.filter { it.imageUrl.isNotBlank() }

                categoryDao.clearCategories()
                categoryDao.insertCategories(validEntities)

                // 3. Emit updated fresh data
                emit(categories)
            } catch (e: Exception) {
                Log.e("UserRepository", "Error fetching from Firebase: ${e.message}", e)
                // Optional: emit(localData) again or not, depends on UX choice
            }
        }
    }
    override suspend fun getMedicinesByCategory(categoryId: String): List<Medicines> {
        val list = firebaseService.getMedicinesBy(categoryId = categoryId)
        return list.map { it.toDomain() }
    }
    override suspend fun getMedicinesByName(name: String): List<Medicines> {
        val list = firebaseService.getMedicinesBy(name = name)
        return list.map { it.toDomain() }
    }
    override suspend fun getMedicine(medId: String): List<Medicines> {
        val list = firebaseService.getMedicinesBy(medicineId = medId)
        return list.map { it.toDomain() }
    }
    override suspend fun getOrders(): Flow<List<Orders>> = flow {

    }
    override suspend fun placeOrder(order: Orders): Flow<Boolean> = flow {

    }
    override fun getShopkeepers(medicineId: String): Flow<List<Shopkeeper>> = flow {
        val list = firebaseService.getPharmacist(medicineId = medicineId)
        emit(list.map { it.toDomain() })
    }
}
//UserRepositoryImpl =
// Manages fetching from both local and remote data sources.
// if (networkChecker.isNetworkAvailable()) firebase else local