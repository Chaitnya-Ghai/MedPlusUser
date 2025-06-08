package com.example.medplus_user.presentation

import kotlinx.serialization.Serializable

@Serializable
data object HomeScreen

@Serializable
data object SearchScreen

@Serializable
data class CategoryScreen(
    val id : String,
    val name : String
)
@Serializable
data class ResultScreen(
    val id : String
)
