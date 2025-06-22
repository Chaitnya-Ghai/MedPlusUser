package com.example.medplus_user.data.remote.dto

import com.google.firebase.firestore.PropertyName

data class ShopkeeperDto(
    val authId: String = "",
    val shopName: String = "",
    val ownerName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val inventory: List<InventoryDto> = emptyList(),
    val medicineId: List<String> = emptyList(),
    val licenseImageUrl: String = "",
    val shopImageUrl: String = "",
    @get:PropertyName("verified")
    @set:PropertyName("verified")
    var isVerified: Int? = 0 // maps "verified" from Firestore
)
