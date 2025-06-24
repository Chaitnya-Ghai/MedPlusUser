package com.example.medplus_user.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medplus_user.common.Constants.Companion.MEDICINE
import com.example.medplus_user.data.remote.dto.ProductDetailDto

@Entity(tableName = MEDICINE)
data class MedicineEntity(
    @PrimaryKey var id :String,
    var medicineName :String?=null,
    var description :String?=null,
    var medicineImg :String?=null,
    var belongingCategory: MutableList<String>?=null,
    var dosageForm:String?=null,
    var unit :String?=null,
    var ingredients :String?=null,
    var howToUse :String?=null,
    var precautions :String?=null,
    var storageInfo :String?=null,
    var sideEffects :String?=null,
    var productDetail: ProductDetailDto?=null
)
