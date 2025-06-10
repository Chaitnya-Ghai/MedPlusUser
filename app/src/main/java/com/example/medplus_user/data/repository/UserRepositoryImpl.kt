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
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val networkChecker: NetworkChecker,
    private val firebaseService: FirebaseService,
    private val categoryDao: CategoryDao,
    private val medicinesDao: MedicinesDao
): UserRepository {
    override  fun getCategories(): Flow<List<Category>> = flow {
        if (networkChecker.isNetworkAvailable()) {
            try {
                val dtoList = firebaseService.getCategories()
                Log.d("UserRepository", "Mapped categories: $dtoList")
                val categories = dtoList.map { it.toDomain() }
                Log.d("UserRepository", "Mapped categories: $categories")

                val entityList = categories.map { it.toEntity() }
                Log.d("UserRepository", "local save categories: $entityList")

                val validCategories = entityList.filter {
                    it.imageUrl.isNotBlank() ==true
                }
                categoryDao.insertCategories(validCategories)

                emit(categories)  // Emit categories to the Flow
            } catch (e: Exception) {
                Log.e("UserRepository", "Error fetching from Firebase: ${e.message}")
                emit(emptyList())  // Emit empty list in case of error
            }
        } else {
            val localEntities = categoryDao.showCategories()
            val data = localEntities.map { it.toDomain() }
            Log.d("UserRepository", "Mapped local data categories: $data")
            emit(data)  // Emit local categories if no network available
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