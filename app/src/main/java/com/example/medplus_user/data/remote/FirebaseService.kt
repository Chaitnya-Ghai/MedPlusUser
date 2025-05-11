package com.example.medplus_user.data.remote

import android.util.Log
import com.example.medplus_user.common.Constants.Companion.category
import com.example.medplus_user.data.remote.dto.CategoryDto
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseService @Inject constructor(){
    val db = Firebase.firestore
    suspend fun getCategories(): List<CategoryDto>{
        return try {
            val snapshot = db.collection(category).get().await()
            snapshot.documents.map { document ->
                document.toObject(CategoryDto::class.java)!!
            }
        }catch (e : Exception){
            Log.e("FirebaseService", "getCategories error = ${e.message}")
            emptyList()
        }
    }
}