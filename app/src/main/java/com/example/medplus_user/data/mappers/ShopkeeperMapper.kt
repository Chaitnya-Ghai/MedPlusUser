package com.example.medplus_user.data.mappers

import com.example.medplus_user.data.remote.dto.InventoryDto
import com.example.medplus_user.data.remote.dto.ShopkeeperDto
import com.example.medplus_user.domain.models.Inventory
import com.example.medplus_user.domain.models.Shopkeeper

fun ShopkeeperDto.toDomain(): Shopkeeper {
    return Shopkeeper(
        id = authId,
        shopName = shopName,
        ownerName = ownerName,
        phoneNumber = phoneNumber,
        address = address,
        latitude = latitude,
        longitude = longitude,
        inventory = inventory.map { it.toDomain() },
        medicineId = medicineId,
        licenseImageUrl = licenseImageUrl,
        shopImageUrl = shopImageUrl,
        isVerified = isVerified
    )
}


fun InventoryDto.toDomain() : Inventory{
    return Inventory(
        medicineId = medicineId,
        medicineName = medicineName,
        shopMedicinePrice = shopMedicinePrice
    )
}