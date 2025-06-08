package com.example.medplus_user.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.style.TextAlign
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
        composable<ResultScreen> {
            val resultScreen = it.toRoute<ResultScreen>()
            ResultScreen(resultScreen.id, navController = navController, viewModel = viewModel)
        }
        composable<CategoryScreen> {
            val categoryScreen = it.toRoute<CategoryScreen>()
            CategoryView(arg = categoryScreen.id,name= categoryScreen.name,navController = navController, viewModel = viewModel)
        }
    }
}


@Composable
fun ResultScreen(arg: String?,navController: NavHostController, viewModel: MainViewModel){
    Row(Modifier.fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
         Text(
             text = "Result Screen $arg ",
         )
     }
}