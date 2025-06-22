package com.example.medplus_user.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.medplus_user.presentation.ui.cardViews.ShopkeeperCard
import com.example.medplus_user.presentation.viewModel.MainViewModel

@Composable
fun SearchShopkeepersScreen( navController : NavHostController , viewModel: MainViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchAllShopkeepers()
    }
    Box(
        modifier = Modifier
            .fillMaxSize().padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFE4D0FF), Color(0xFFFFE5EC))
                )
            )
    ){
        val filteredList by viewModel.filteredShopkeepers.collectAsState()
        val searchQuery by viewModel.shopQuerySearch.collectAsState()
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(8.dp))
            SearchHeader(
                title = "Search Shopkeepers",
                onBack = { navController.popBackStack() }
            )

            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.updateQueryShop(searchQuery)} ,
                onFilterClick = {/* show options*/}
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                .fillMaxWidth()
                .padding(end = 14.dp), horizontalArrangement = Arrangement.End , verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Clear All",
                    color = Color.Gray,
                    modifier = Modifier
                    )
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search",
                    modifier = Modifier
                        .clickable {}
                        .size(18.dp)
                        .alpha(0.4f)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ){
                items(filteredList) {shopkeeper ->
                    Log.e("Shopkeeper", "SearchShopkeepersScreen: ${shopkeeper.id}")
                    ShopkeeperCard(shopkeeper)
                }
                item { Spacer(modifier = Modifier.height(180.dp)) }
            }
        }
    }
}

