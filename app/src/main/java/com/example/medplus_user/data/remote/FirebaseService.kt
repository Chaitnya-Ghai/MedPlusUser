package com.example.medplus_user.data.remote

import android.util.Log
import com.example.medplus_user.common.Constants.Companion.MEDICINE
import com.example.medplus_user.common.Constants.Companion.PHARMACIST
import com.example.medplus_user.data.remote.dto.CategoryDto
import com.example.medplus_user.data.remote.dto.MedicineDto
import com.example.medplus_user.data.remote.dto.ShopkeeperDto
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseService @Inject constructor(){
    val db = Firebase.firestore
    suspend fun getCategories(): List<CategoryDto> {
        return try {
            val snapshot = db.collection("category").get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(CategoryDto::class.java)
                } catch (e: Exception) {
                    Log.e("FirebaseService", "Deserialization failed: ${doc.data}", e)
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseService", "getCategories error = ${e.message}")
            emptyList()
        }
    }

    suspend fun getMedicines(): List<MedicineDto>{
        return try {
            val snapshot = db.collection(MEDICINE).get().await()
            snapshot.documents.mapNotNull { document -> document.toObject(MedicineDto::class.java) }
        }catch (e: Exception){
            Log.e("FirebaseService", "getMedicines error = ${e.message}")
            emptyList()
        }
    }

    suspend fun getMedicinesBy(
        medicineId: String? = null,
        categoryId: String? = null,
        name: String? = null
    ): List<MedicineDto> {
        return try {
            val query = when {
                medicineId != null -> db.collection("medicines")
                    .whereEqualTo("id", medicineId)

                categoryId != null -> db.collection("medicines")
                    .whereArrayContains("belongingCategory", categoryId)

                name != null -> db.collection("medicines")
                    .whereEqualTo("medicineName", name)

                else -> db.collection("medicines")
            }
            val snapshot = query.get().await()
            snapshot.documents.mapNotNull {
                it.toObject(MedicineDto::class.java)
            }
        } catch (e: Exception) {
            Log.e("FirebaseService", "getMedicinesBy error = ${e.message}")
            emptyList()
        }
    }


    suspend fun getShopkeeperFromMedicineId(medicineId: String): List<ShopkeeperDto>{
        return try {
            val snapshot = db.collection(PHARMACIST).whereArrayContains("medicineId",medicineId)
                .get().await()
            snapshot.documents.mapNotNull { it.toObject(ShopkeeperDto::class.java) }
        }catch (e: Exception){
            Log.e("FirebaseService", "getPharmacist error = ${e.message}")
            emptyList()
        }
    }

    suspend fun getAllShopkeepers(): List<ShopkeeperDto>{
            val db= db.collection(PHARMACIST).get().await()
            return db.documents.mapNotNull { it.toObject(ShopkeeperDto::class.java) }
    }

}