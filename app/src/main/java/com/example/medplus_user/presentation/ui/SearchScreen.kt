package com.example.medplus_user.presentation.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.presentation.CategoryScreen
import com.example.medplus_user.presentation.viewModel.MainViewModel

@Composable
fun SearchView(viewModel: MainViewModel, navController: NavHostController){
    val categories by viewModel.categories.collectAsState(emptyList())
    Column(modifier = Modifier.fillMaxSize()) {
        SearchField(categories, navController)
        MainBody(categories=categories,navController = navController)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(categories: List<Category>, navController: NavHostController) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally //This centers children horizontally
    ) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(0.95f), // Slight margin from edges if desired
            inputField = {
                InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = {},
                    expanded = expanded,
//                    Lambda to handle changes in the dropdown's expanded state.
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search...") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { item ->
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(60.dp)
                            .width(100.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = item.categoryName.toString(),
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainBody(categories: List<Category> , navController: NavController){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxHeight(1f) // this bounds the height
            .padding(5.dp),
        userScrollEnabled = true
    ) {
        items(categories) { item ->
            Column(
                modifier = Modifier.padding(8.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CategoryCardView(item , navController = navController)
            }
        }
    }
}




@Composable
fun CategoryCardView(item: Category , navController: NavController) = Card(
    modifier = Modifier
        .fillMaxSize()
        .aspectRatio(1f)
        .clickable {
            Log.e("Ui Event", "${item.categoryName} clicked")
            navController.navigate( CategoryScreen( item.id.toString() , item.categoryName) )
                   },
    shape = RoundedCornerShape(8.dp),
    elevation = CardDefaults.elevatedCardElevation(8.dp)
) {
    Image(
        painter = rememberAsyncImagePainter(model = item.imageUrl),
        contentDescription = "Image of ${item.categoryName}",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
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
// UI is not finalized yet,
// so dummy data and dummy UI are being used for now.