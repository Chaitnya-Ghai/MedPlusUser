package com.example.medplus_user.presentation

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.medplus_user.presentation.ui.CategoryView
import com.example.medplus_user.presentation.ui.CustomBottomBar
import com.example.medplus_user.presentation.ui.HomeView
import com.example.medplus_user.presentation.ui.SearchMedicinesScreen
import com.example.medplus_user.presentation.ui.SearchShopkeepersScreen
import com.example.medplus_user.presentation.ui.ShopkeeperShopScreen
import com.example.medplus_user.presentation.viewModel.MainViewModel

@Composable
fun Navigation(navController: NavHostController) {
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = { CustomBottomBar(navController) }
    ){
        innerPadding ->
        Log.e("TAG", "Navigation: $innerPadding" )
        val viewModel: MainViewModel = hiltViewModel()
        viewModel.categories
        NavHost(navController = navController, startDestination = HomeScreen) {
            composable<HomeScreen> {
                HomeView(navController = navController, viewModel = viewModel)
            }
            composable<CategoryScreen> {
                val categoryScreen = it.toRoute<CategoryScreen>()
                CategoryView(catId = categoryScreen.id,categoryName= categoryScreen.name,navController = navController, viewModel = viewModel)
            }
            composable<SearchMedicinesScreen> {
                SearchMedicinesScreen(navController = navController , viewModel = viewModel)
            }
            composable <SearchShopkeepersScreen>{
                SearchShopkeepersScreen( navController = navController, viewModel = viewModel)
            }

            composable<ShopkeeperShopScreen> {
                val shopkeeperShopScreen = it.toRoute<ShopkeeperShopScreen>()
                ShopkeeperShopScreen(shopkeeperShopScreen.id, navController = navController, viewModel = viewModel)
            }
//            composable<MedicineScreen> {
//                val medicineScreen = it.toRoute<MedicineScreen>()
//                MedicineView(medicineId = medicineScreen.id , shopMedicinePrice = medicineScreen.shopMedicinePrice, navController = navController, viewModel = viewModel)
//
//            }
        }
    }
}