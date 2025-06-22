package com.example.medplus_user.presentation.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import com.example.medplus_user.R
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.medplus_user.common.Resource
import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.models.Shopkeeper
import com.example.medplus_user.presentation.SearchMedicinesScreen
import com.example.medplus_user.presentation.ui.cardViews.CategoryCard
import com.example.medplus_user.presentation.ui.cardViews.ShopkeeperCard
import com.example.medplus_user.presentation.viewModel.MainViewModel
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun HomeView(
    navController: NavController,
    viewModel: MainViewModel,
) {
    val context = LocalContext.current
    val categoryResource by viewModel.categories.collectAsState()

    // Collect and cache list based on Resource state
    val categoryList = remember(categoryResource) {
        when (categoryResource) {
            is Resource.Success -> categoryResource.data
            is Resource.Error -> {
                Toast.makeText(context, categoryResource.message, Toast.LENGTH_SHORT).show()
                emptyList()
            }
            is Resource.Loading -> emptyList()
        }
    }

    // 🔁 Preload category images when data changes
    LaunchedEffect(key1 = categoryList) {
        if (!categoryList.isNullOrEmpty()) {
            val imageLoader = ImageLoader(context)
            categoryList.forEach { category ->
                category.imageUrl.takeIf { it.isNotBlank() }?.let { url ->
                    imageLoader.enqueue(
                        ImageRequest.Builder(context)
                            .data(url)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .allowHardware(false)
                            .build()
                    )
                }
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize().padding(WindowInsets.statusBars.asPaddingValues())
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE4D0FF), Color(0xFFFFE5EC))
                )
            ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item { HomeHeaderSection() }
        item { SearchField( onSearchBarClick = { navController.navigate(SearchMedicinesScreen) } ) }
        item { RandomShops() }
        item{
            when {
                !categoryList.isNullOrEmpty() -> {
                    // Safely pass a non-null list
                    CategoryGrid(
                        categories = categoryList,
                        navController = navController
                    )
                }

                categoryResource is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(40.dp))
                    }
                }

                else -> {
                    Text(
                        text = "No categories available",
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        item {
            Text(
                text = " Promotions",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        item { RandomShops() }
        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}


@Composable
fun ShopkeepersNearMe(shopkeepers: List<Shopkeeper>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.shopkeeper_profile),
                contentDescription = null,
                modifier = Modifier.size(55.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Shopkeepers near me", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        shopkeepers.forEach { shopkeeper ->
            ShopkeeperCard(shopkeeper)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}



@Composable
fun HomeHeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp) // consistent external padding
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = "Track and order your medicines easily",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            // Cart Icon
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .clickable { Log.e("Debug", "Cart clicked") },
                tint = Color(0xFF5C9EFF)
            )

            // Profile Icon
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier
                    .padding(start = 6.dp)
                    .clickable { Log.e("Debug", "Profile clicked") },
                tint = Color(0xFF5C9EFF)
            )
        }
    }
}

@Composable
fun SearchField(onSearchBarClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(6.dp, RoundedCornerShape(8.dp))
            .background(Color(0xFFF8F4F4), RoundedCornerShape(8.dp))
            .clickable { onSearchBarClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "@null",
                modifier = Modifier.padding(12.dp),
                tint = Color.Gray
            )
            Text(
                text = "Search...",
                modifier = Modifier.padding(10.dp),
                color = Color.Gray
            )
        }
    }
}




@Composable
fun CategoryGrid(
    categories: List<Category>,
    navController: NavController
) {

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Categories",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // First Grid (2 items max)
        val firstTwo = categories.take(2)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LazyRow(
                modifier = Modifier
                    .widthIn(200.dp)
                    .heightIn(max = 170.dp),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {

                items(firstTwo) { category ->
                    CategoryCard(
                        category = category,
                        navController = navController,
                        modifier = Modifier
                            .height(80.dp)
                            .width(80.dp)
                            .aspectRatio(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Remaining Grid (groups of 4)
        val remaining = categories.drop(2)
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(remaining) { category ->
                CategoryCard(
                    category = category,
                    navController = navController,
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
        }
    }
}

@Composable
fun rememberCustomImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember {
        ImageLoader.Builder(context)
            .diskCache {
                DiskCache.Builder()
                    .directory(File(context.cacheDir, "image_cache"))
                    .maxSizeBytes(50L * 1024 * 1024) // 50MB cache
                    .build()
            }
            .crossfade(true)
            .respectCacheHeaders(false) // <- IMPORTANT
            .build()
    }
}


@Composable
fun RandomShops() {
    val list = listOf(
        R.drawable.promotion1,
        R.drawable.promotion2,
        R.drawable.promotion3,
        R.drawable.promotion4,
        R.drawable.promotion5
    )

    val listState = rememberLazyListState()
    val currentIndex = rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            currentIndex.intValue = (currentIndex.intValue + 1) % list.size
            listState.animateScrollToItem(currentIndex.intValue)
        }
    }
    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(list) { item ->
            Card(
                modifier = Modifier
                    .width(365.dp)
                    .height(230.dp)
                    .clickable { Log.e("Debug", "Card click: $item") },
                shape = RoundedCornerShape(30.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Image(
                    painter = painterResource(id = item),
                    contentDescription = "Promotional Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
