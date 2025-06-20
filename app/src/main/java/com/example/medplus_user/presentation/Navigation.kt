package com.example.medplus_user.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.medplus_user.presentation.ui.CategoryView
import com.example.medplus_user.presentation.ui.HomeView
import com.example.medplus_user.presentation.ui.MedicineView
import com.example.medplus_user.presentation.ui.ShopkeeperResultsScreen
import com.example.medplus_user.presentation.ui.SearchView
import com.example.medplus_user.presentation.viewModel.MainViewModel

@Composable
fun Navigation(navController: NavHostController) {
    val viewModel: MainViewModel = hiltViewModel()
    var selectedIndex = rememberSaveable { mutableStateOf(0) }


    NavHost(navController = navController, startDestination = HomeScreen) {
        composable<HomeScreen> {
            HomeView(navController = navController, viewModel = viewModel)
        }
        composable<SearchScreen> {
            SearchView(navController = navController, viewModel = viewModel)
        }
        composable<ShopkeeperResultScreen> {
            val shopkeeperResultScreen = it.toRoute<ShopkeeperResultScreen>()
            ShopkeeperResultsScreen(shopkeeperResultScreen.id, navController = navController, viewModel = viewModel)
        }
        composable<CategoryScreen> {
            val categoryScreen = it.toRoute<CategoryScreen>()
            CategoryView(medId = categoryScreen.id,name= categoryScreen.name,navController = navController, viewModel = viewModel)
        }
        composable<MedicineScreen> {
            val medicineScreen = it.toRoute<MedicineScreen>()
            MedicineView(medicineId = medicineScreen.id , shopMedicinePrice = medicineScreen.shopMedicinePrice, navController = navController, viewModel = viewModel)

        }

    }
}