package com.example.medplus_user.data.local.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medplus_user.data.local.dao.CategoryDao
import com.example.medplus_user.data.local.dao.MedicinesDao
import com.example.medplus_user.data.local.entity.CategoryEntity
import com.example.medplus_user.data.local.entity.MedicineEntity

@Database(entities = [CategoryEntity::class , MedicineEntity::class], version = 1, exportSchema = true)
@TypeConverters(ProductDetailConverter::class, Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun CategoryDao(): CategoryDao
    abstract fun MedicinesDao(): MedicinesDao
}