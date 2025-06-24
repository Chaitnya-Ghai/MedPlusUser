package com.example.medplus_user.presentation.common

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.medplus_user.presentation.HomeScreen
import com.example.medplus_user.presentation.SearchShopkeepersScreen

@Composable
fun CustomBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.Transparent,
        modifier = Modifier.shadow(8.dp)
            .background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFD3E0E8),
                    Color(0xFFD3E0E8)
                )
            )
        ),
        tonalElevation = 12.dp
    )
    {
        NavigationBarItem(
            selected = currentRoute == HomeScreen::class.qualifiedName,
            onClick = {
                if (currentRoute != HomeScreen.toString()) {
                    navController.navigate(HomeScreen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home" ) },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E88E5),     // Deep Blue
                unselectedIconColor = Color(0xFF8E8E93),   // Gray
                selectedTextColor = Color(0xFF1E88E5),
                unselectedTextColor = Color(0xFF8E8E93),
                indicatorColor = Color(0xFFD6EAF8)
            )
        )
        NavigationBarItem(
            selected = currentRoute == SearchShopkeepersScreen::class.qualifiedName,
            onClick = {
                if (currentRoute != SearchShopkeepersScreen.toString()) {
                    navController.navigate(SearchShopkeepersScreen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Search, contentDescription = "Search" ) },
            label = { Text("Search") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E88E5),     // Deep Blue
                unselectedIconColor = Color(0xFF8E8E93),   // Gray
                selectedTextColor = Color(0xFF1E88E5),
                unselectedTextColor = Color(0xFF8E8E93),
                indicatorColor = Color(0xFFD6EAF8)         // Light Blue Ripple
            )
        )
    }
}
