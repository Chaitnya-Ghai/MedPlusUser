package com.example.medplus_user.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.medplus_user.common.Resource
import com.example.medplus_user.presentation.common.cardViews.MedicineCard
import com.example.medplus_user.presentation.viewModel.MainViewModel

@Composable
fun CategoryView(
    catId: String?,
    categoryName: String,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val searchQuery by viewModel.categorySpecificMedicinesQuery.collectAsState()
    val categoryMedicinesResource by viewModel.categorySpecificMedicines.collectAsState()
    val filteredMedicines by viewModel.filteredCategoryMedicines.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMedicinesByCategory(catId ?: "")
    }

    Column(
        modifier = Modifier
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf( Color(0xFFE4D0FF) , Color(0xFFFFE5EC) )
                )
            )
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchHeader(title = categoryName) {
            navController.popBackStack()
        }

        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.updateCategorySpecificMedicinesQuery(it) },
            onFilterClick = {}
        )
val gridState = rememberLazyGridState()
        when (categoryMedicinesResource) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Error -> {
                Toast.makeText(context, categoryMedicinesResource.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
            is Resource.Success -> {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed( filteredMedicines ,
                        span = { index, _ ->
                            if ((index + 1) % 5 == 0) GridItemSpan(2) else GridItemSpan(1)
                        }) { index, item ->
                        val isFullSpan = (index + 1) % 5 == 0
                        MedicineCard(med = item, isFullSpan = isFullSpan)
                    }
                    item { Spacer(modifier = Modifier.height(180.dp)) }
                }
            }
        }
    }
}




