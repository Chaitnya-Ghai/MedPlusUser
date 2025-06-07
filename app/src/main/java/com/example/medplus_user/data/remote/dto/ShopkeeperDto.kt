package com.example.medplus_user.data.remote.dto

data class ShopkeeperDto(
    val authId: String = "",
    val shopName: String = "",
    val ownerName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val inventory: List<InventoryDto> = emptyList(),
    val medicineId: List<String> = emptyList(),//for faster query
    val licenseImageUrl: String = "",
    val shopImageUrl: String = "",
    var isVerified: Int ?=0,  // 0 for not Register , 1 for not verified, 2 for verified , 3 for rejected
)
