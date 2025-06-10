package com.example.medplus_user.domain.models

import kotlinx.serialization.Contextual

data class Shopkeeper(
    val id: String = "",
    val shopName: String = "",
    val ownerName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val inventory: List<Inventory> = emptyList(),
    val medicineId: List<String> = emptyList(),//for faster query
    val licenseImageUrl: String = "",
    val shopImageUrl: String = "",
    var isVerified: Int ?=0,
)
data class Inventory(
    var medicineId: String = "",
    var medicineName: String = "",
    var shopMedicinePrice: String = "",
)
