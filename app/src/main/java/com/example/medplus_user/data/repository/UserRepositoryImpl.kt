package com.example.medplus_user.data.repository

import android.util.Log
import com.example.medplus_user.common.NetworkChecker
import com.example.medplus_user.common.Resource
import com.example.medplus_user.data.local.dao.CategoryDao
import com.example.medplus_user.data.local.dao.MedicinesDao
import com.example.medplus_user.data.mappers.toDomain
import com.example.medplus_user.data.mappers.toEntity
import com.example.medplus_user.data.remote.FirebaseService
import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.models.Medicines
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

    override fun getCategories(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading()) // show loading

        val localEntities = categoryDao.showCategories()
        val localData = localEntities.map { it.toDomain() }

        if (localData.isNotEmpty()) {
            emit(Resource.Success(localData)) // emit cached data
        }

        if (networkChecker.isNetworkAvailable()) {
            try {
                val dtoList = firebaseService.getCategories()
                val categories = dtoList.map { it.toDomain() }
                val entities = categories.map { it.toEntity() }
                val validEntities = entities.filter { it.imageUrl.isNotBlank() }

                categoryDao.clearCategories()
                categoryDao.insertCategories(validEntities)

                emit(Resource.Success(categories)) // emit fresh data
            } catch (e: Exception) {
                if (localData.isEmpty()) {
                    Log.e("TAG", "getCategories: ${e.message}", )
                    emit(Resource.Error( "Unable to load categories. Please check your internet connection."))
                }
            }
        } else if (localData.isEmpty()) {
            emit(Resource.Error("No categories available offline. Please connect to the internet."))
        }
    }

    override suspend fun getAllMedicines(): Resource<List<Medicines>> {
        return try {
            val dtoList = firebaseService.getMedicines()
            val allMedicines = dtoList.map { it.toDomain() }
            Resource.Success(allMedicines)
        }catch (e: Exception){
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getAllShopkeepers(): Resource<List<Shopkeeper>> {
        return try {
            val dtoList = firebaseService.getAllShopkeepers()
            val modelList = dtoList.map{ it.toDomain() }
            Resource.Success(modelList)
        }catch (e : Exception){
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getMedicinesByCategory(catId: String): Resource<List<Medicines>> {
        return try {
            val dtoList = firebaseService.getMedicinesBy(categoryId = catId)
            val modelList = dtoList.map{ it.toDomain() }
            Resource.Success(modelList)
        }catch (e: Exception){
            Resource.Error(e.message ?: "Unknown error")
        }
    }


}