package com.example.medplus_user.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.medplus_user.data.local.entity.CategoryEntity

@Dao
interface CategoryDao {
//
    @Query("SELECT * FROM category")
    suspend fun showCategories(): List<CategoryEntity>

    @Query("SELECT * FROM category WHERE categoryName = :name")
    suspend fun findCategoryByName(name:String) : CategoryEntity

    @Upsert
    suspend fun insertCategories(categories: List<CategoryEntity>)

}