package com.example.medplus_user.data.local.localDatabase

import androidx.room.TypeConverter
import com.example.medplus_user.data.remote.dto.ProductDetailDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductDetailConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromProductDetailDto(product: ProductDetailDto): String {
        return gson.toJson(product)
    }

    @TypeConverter
    fun toProductDetailDto(json: String): ProductDetailDto {
        val type = object : TypeToken<ProductDetailDto>() {}.type
        return gson.fromJson(json, type)
    }
}