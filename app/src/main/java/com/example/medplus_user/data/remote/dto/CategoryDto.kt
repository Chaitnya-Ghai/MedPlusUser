package com.example.medplus_user.data.remote.dto

data class CategoryDto(
    var id: String,
    var categoryName: String,
    var imageUrl: String
){
    // Add a no-argument constructor
    constructor() : this("", "", "")
}


