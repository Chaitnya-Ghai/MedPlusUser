package com.example.medplus_user.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.medplus_user.R
import com.example.medplus_user.presentation.common.cardViews.MedicineCard
import com.example.medplus_user.presentation.viewModel.MainViewModel

@Composable
fun SearchMedicinesScreen(navController: NavController , viewModel: MainViewModel ) {
    LaunchedEffect(Unit) {
        viewModel.fetchAllMedicines()
    }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredList by viewModel.filteredMedicines.collectAsState()

    val gridState = rememberLazyGridState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf( Color(0xFFE4D0FF) , Color(0xFFFFE5EC) )
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchHeader(
                title = "Search Medicines",
                onBack = { navController.popBackStack() }
            )

            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.updateQueryMed(it) },
                onFilterClick = {
                    Toast.makeText(context, "Filter clicked", Toast.LENGTH_SHORT).show()
                }
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed( filteredList ,
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


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    val isLabelVisible = query.isEmpty()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp)
            .shadow(6.dp, RoundedCornerShape(8.dp))
            .background(Color(0xFFF8F4F4), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { if (isLabelVisible) Text("Search...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.DarkGray,
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.filter),
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier.clickable { onFilterClick() }
                )
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
    }
}
