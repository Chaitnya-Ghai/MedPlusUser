package com.example.medplus_user.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.medplus_user.presentation.ui.CategoryView
import com.example.medplus_user.presentation.ui.HomeView
import com.example.medplus_user.presentation.ui.SearchView
import com.example.medplus_user.presentation.viewModel.MainViewModel

@Composable
fun Navigation(navController: NavHostController) {
    val viewModel: MainViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = HomeScreen) {
        composable<HomeScreen> {
            HomeView(navController = navController, viewModel = viewModel)
        }
        composable<SearchScreen> {
            SearchView(navController = navController, viewModel = viewModel)
        }
        composable<CategoryScreen> {
            val id = it.toRoute<CategoryScreen>()
            CategoryView(args = id.toString(),navController = navController,viewModel= viewModel)
        }
    }
}