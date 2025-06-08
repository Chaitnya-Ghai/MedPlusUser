package com.example.medplus_user.presentation.ui

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.medplus_user.presentation.CategoryScreen
import com.example.medplus_user.presentation.SearchScreen
import com.example.medplus_user.ui.theme.MedPlusUserTheme
import com.example.medplus_user.presentation.viewModel.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeView(
    viewModel: MainViewModel,
    navController: NavController
) {
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

    if (moveUp) {
        LaunchedEffect(Unit) {
            delay(200)
            navController.navigate(SearchScreen)
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(Icons.Filled.Home, contentDescription = "Home View")
                        }
                        IconButton(onClick = { navController.navigate(SearchScreen) }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search View")
                        }
                        IconButton(onClick = { /* do something */ }) {
                            Icon(Icons.Filled.FavoriteBorder, contentDescription = "Favorite View")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .blur(blurAmount.dp)
                .padding(WindowInsets.systemBars.asPaddingValues())
        ) {
            item { HeaderSection(navController) }
            item { LocationSection(viewModel) }
            item { SearchBar(moveUp, { moveUp = true }) }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item { RandomShops()} // auto-scroll slider
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item { CategoriesSection(viewModel,navController) }
            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}

@Composable
fun HeaderSection(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable{ /* open bottom sheet */}
    ) {
        Text(
            text = "Welcome",
            textAlign = TextAlign.Justify,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "open cart",
            modifier = Modifier
                .padding(end = 6.dp)
                .clickable { Log.e("Debug", "cart click: ") },
            tint = Color.Gray
        )
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "open profile",
            modifier = Modifier
                .padding(end = 6.dp)
                .clickable { Log.e("Debug", "profile click: ") },
            tint = Color.Gray
        )
    }
}

@Composable
fun LocationSection(viewModel : MainViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val location by viewModel.location.collectAsState()
    val address by viewModel.address.collectAsState()
//    location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
            isGranted ->
        if (isGranted) {
            //User gave permission: proceed to get location
            viewModel.fetchLocation()
        } else {
            //User denied
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Row(
        modifier = Modifier.clickable{
            Log.e("LocationProvider in Home", "location click:${location?.latitude} ${location?.longitude} ")
        }
    ) {
        viewModel.getAddressLine(location?.latitude,location?.longitude)
        Text(
            text = "$address",
            modifier = Modifier.padding(12.dp)
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "open location",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable { Log.e("Debug", "location click: ") },
            tint = Color.Gray
        )
    }
}

@Composable
fun SearchBar(moveUp: Boolean, onSearchBarClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .offset(y = if (moveUp) (-80.dp) else 0.dp)
            .clickable { onSearchBarClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search icon",
                modifier = Modifier.padding(12.dp),
                tint = Color.Gray
            )
            Text(
                text = "Search...",
                modifier = Modifier.padding(10.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CategoriesSection(viewModel: MainViewModel , navController: NavController) {
    Text(text = "Categories", modifier = Modifier.padding(12.dp), fontSize = 24.sp)
    val categories by viewModel.categories.collectAsState(emptyList())
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(5.dp),
        userScrollEnabled = false
    ) {
        items(categories) { item ->
            Column (
                modifier = Modifier.padding(8.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .clickable {
                            Log.e("Ui Event", "${item.categoryName} clicked")
                                   navController.navigate(CategoryScreen(item.id.toString() , item.categoryName.toString()))
                                   },
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.elevatedCardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = item.imageUrl),
                            contentDescription = "Image of ${item.categoryName}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(top = 5.dp))
                Text(
                    text = item.categoryName.toString(),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }

        }

    }

}

@Composable
fun RandomShops() {
//      ess mai list ayegii joh random shops ki hoyegi.
    val list = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
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
//            category display composable
            Card(
                modifier = Modifier
                    .width(250.dp)
                    .height(160.dp)
                    .clickable {
                        Log.e("Debug", "Card click: $item")
                    },
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = item, fontSize = 24.sp)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PrevCard(){
    MedPlusUserTheme {
        RandomShops()
    }
}