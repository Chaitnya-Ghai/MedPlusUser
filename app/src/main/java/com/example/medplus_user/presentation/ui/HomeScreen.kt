package com.example.medplus_user.presentation.ui

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.disk.DiskCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.models.Shopkeeper
import com.example.medplus_user.presentation.CategoryScreen
import com.example.medplus_user.presentation.SearchScreen
import com.example.medplus_user.presentation.viewModel.MainViewModel
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun HomeView(
    navController: NavController,
    viewModel: MainViewModel,
) {
    val shopkeepers  = viewModel.shopkeepers.collectAsState()//dummy data
    val categoryList: State<List<Category>> = viewModel.categories.collectAsState()
    var moveUp by remember { mutableStateOf(false) }
    val offset by animateDpAsState(
        targetValue = if (moveUp) (-80.dp) else 0.dp,
        label = "SearchBarOffset",
        animationSpec = tween(durationMillis = 700)
    )
    val blurAmount by animateFloatAsState(
        targetValue = if (moveUp) 5f else 0f,
        label = "BackgroundBlur",
        animationSpec = tween(durationMillis = 700)
    )
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.categories.value.forEach {
            ImageLoader(context).enqueue(
                ImageRequest.Builder(context)
                    .data(it.imageUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .allowHardware(false)
                    .build()
            )
        }
    }

    if (moveUp) {
        LaunchedEffect(Unit) {
            delay(300)
            navController.navigate(SearchScreen)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = { CustomBottomBar(navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFE4D0FF), Color(0xFFFFE5EC))
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item{ HeaderSection() }
            item { SearchField(moveUp = moveUp, onSearchBarClick = { moveUp = true }) }
            item { RandomShops() }
            item { CategoryGrid(categoryList.value , navController = navController) }
            item{
                Text(
                    text = "Promotions",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            item { RandomShops() }
            item { ShopkeepersNearMe(shopkeepers.value) }
            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
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
fun ShopkeeperCard(shopkeeper: Shopkeeper) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray.copy(alpha = 0.23f))
    ) {
        Image(
            painter = rememberAsyncImagePainter(shopkeeper.shopImageUrl),
            contentDescription = shopkeeper.shopName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxHeight()
                .width(160.dp)
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(shopkeeper.shopName, color = Color.White, fontWeight = FontWeight.Bold)
                Text(shopkeeper.address, color = Color.White, fontSize = 12.sp)
                Text("~ ${shopkeeper.ownerName}", color = Color.LightGray, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun CustomBottomBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.Transparent,
        modifier = Modifier.background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFD3E0E8),
                    Color(0xFFD3E0E8),                )
            )
        ),
        tonalElevation = 12.dp
    )
    {
        NavigationBarItem(
            selected = false,
            onClick = { /* handle */ },
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
            selected = true,
            onClick = { /**/ },
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

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier.padding(top = 16.dp)
    ){
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
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
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                modifier = Modifier
                    .clickable { Log.e("Debug", "Cart clicked") }
                    .padding(end = 6.dp),
                tint = Color(0xFF5C9EFF)
            )
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier
                    .clickable { Log.e("Debug", "Profile clicked") }
                    .padding(end = 6.dp),
                tint = Color(0xFF5C9EFF)
            )
        }
    }
}

@Composable
fun SearchField(moveUp: Boolean, onSearchBarClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(6.dp, RoundedCornerShape(8.dp))
            .background(Color(0xFFF8F4F4), RoundedCornerShape(8.dp))
            .offset(y = if (moveUp) (-80.dp) else 0.dp)
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
fun CategoryCard(
    category: Category,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable {
                navController.navigate(
                    CategoryScreen(
                        category.id.toString(),
                        category.categoryName.toString()
                    )
                )
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F4F4))
    ) {
        val imageLoader = rememberCustomImageLoader()
        val safeUrl = category.imageUrl.replace(" ", "%20")//Medplus Admin  // space in link cause issue, so replace with %20
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(safeUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .listener(
                        onError = { request, result ->
                            Log.e("CoilError", "Image failed: ${category.imageUrl}", result.throwable)
                        },
                        onSuccess = { _, _ ->
                            Log.d("CoilSuccess", "Image loaded: ${category.imageUrl}")
                        }
                    )
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .error(R.drawable.error)
                    .build(),
                imageLoader = imageLoader // Use custom loader here
            ),
            contentDescription = category.categoryName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
        )
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
