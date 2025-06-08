package com.example.medplus_user.data.mappers

import com.example.medplus_user.data.remote.dto.MedicineDto
import com.example.medplus_user.data.remote.dto.ProductDetailDto
import com.example.medplus_user.domain.models.Medicines
import com.example.medplus_user.domain.models.ProductDetail

    fun MedicineDto.toDomain(): Medicines {
        return Medicines(
            id = id,
            medicineName = medicineName,
            description = description,
            medicineImg = medicineImg,
            belongingCategory = belongingCategory,
            dosageForm = dosageForm,
            unit = unit,
            ingredients = ingredients,
            howToUse = howToUse,
            precautions = precautions,
            storageInfo = storageInfo,
            sideEffects = sideEffects,
            productDetail = productDetail?.toDomain()
        )
    }
    fun ProductDetailDto.toDomain(): ProductDetail {
        return ProductDetail(
            brandName = brandName,
            originalPrice = originalPrice,
            expiryDate = expiryDate
        )
    }
