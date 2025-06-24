package com.example.medplus_user.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medplus_user.common.Constants.Companion.CATEGORY

@Entity(tableName = CATEGORY)
data class CategoryEntity(
    @PrimaryKey var id: String,
    var categoryName: String,
    var imageUrl: String
)

