package com.example.medplus_user.data.mappers

import com.example.medplus_user.data.local.entity.CategoryEntity
import com.example.medplus_user.data.remote.dto.CategoryDto
import com.example.medplus_user.domain.models.Category
import java.util.UUID

// CategoryMapper.kt
// DTO -> Domain
fun CategoryDto.toDomain(): Category {
    return Category(
        id = id,
        categoryName = categoryName,
        imageUrl = imageUrl  // Include imageUrl
    )
}

// DTO -> Entity
fun CategoryDto.toEntity(): CategoryEntity? {
    val safeId = id
    return if (!safeId.isNullOrBlank()) {
        CategoryEntity(
            id = safeId,
            categoryName = categoryName,
            imageUrl = imageUrl
        )
    } else {
        null
    }
}



// Entity -> Domain
fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        categoryName = categoryName,
        imageUrl = imageUrl  // Include imageUrl
    )
}

// Domain -> Entity (optional, useful for inserting Domain into Room)
fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id ?: "",  // Ensure non-null value for id
        categoryName = categoryName,
        imageUrl = imageUrl  // Include imageUrl here
    )
}

// Entity -> DTO (optional, useful if syncing back to Firebase)
fun CategoryEntity.toDto(): CategoryDto {
    return CategoryDto(
        id = id,
        categoryName = categoryName,
        imageUrl = imageUrl  // Include imageUrl
    )
}
