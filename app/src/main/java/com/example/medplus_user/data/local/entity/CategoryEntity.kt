package com.example.medplus_user.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medplus_user.common.Constants.Companion.category

@Entity(tableName = category)
data class CategoryEntity(
    @PrimaryKey var id: String = "",
    var categoryName: String? = null,
    var imageUrl: String? = null  // Ensure imageUrl is included here
)

