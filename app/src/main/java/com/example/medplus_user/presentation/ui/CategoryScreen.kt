package com.example.medplus_user.presentation.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.medplus_user.domain.models.Medicines
import com.example.medplus_user.presentation.ResultScreen
import com.example.medplus_user.presentation.viewModel.MainViewModel

@Composable
fun CategoryView(
    arg: String?, name: String,
    navController: NavHostController,
    viewModel: MainViewModel
) {

    val query = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        Log.e("CategoryScreen", "CategoryView: $arg " )
        viewModel.getMedicinesByCategory(catId = arg.toString())
    }

    val medicineList by viewModel.medicinesByCat.collectAsState()

    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.padding(top = 26.dp))
        Text(text = name, modifier = Modifier.padding(12.dp), fontSize = 24.sp)
        Spacer(modifier = Modifier.padding(12.dp))
        SearchBar(query)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxHeight(1f) // this bounds the height
                .padding(5.dp),
            userScrollEnabled = true
        ) {
            if (query.value.isBlank()) {
                items(medicineList) { medicine ->
                    MedicineCard(medicine , navController = navController)
                }
            }
            else{
                items(medicineList.filter { it.medicineName.toString().contains(query.value) }) { medicine ->
                    MedicineCard(medicine , navController )
                }
            }

        }
    }
}


@Composable
fun MedicineCard(item: Medicines , navController: NavController) = Card(
    modifier = Modifier
        .fillMaxSize()
        .aspectRatio(1f)
        .clickable {
            Log.e("Ui Event", "${item.medicineName} clicked")
            navController.navigate( ResultScreen(item.medicineName.toString()))//this screen contains medicines from local shopkeepers
                   },
    shape = RoundedCornerShape(8.dp),
    elevation = CardDefaults.elevatedCardElevation(8.dp)
) {
    Image(
        painter = rememberAsyncImagePainter(model = item.medicineImg),
        contentDescription = "Image of ${item.medicineName}",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Text(
        text = item.medicineName.toString(),
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

@Composable
fun SearchBar(query: MutableState<String>) {
    OutlinedTextField(
        value = query.value,
        onValueChange = { query.value = it },
        placeholder = { Text("Search...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon"
            )
        },
        trailingIcon = {
            if (query.value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear icon",
                    modifier = Modifier
                        .clickable { query.value = "" }
                        .padding(end = 8.dp)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(70.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        textStyle = TextStyle(fontSize = 16.sp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.LightGray
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    val query = remember { mutableStateOf("Paracetamol") }

    // Optional: Wrap in a MaterialTheme and Column to preview nicely
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SearchBar(query = query)
        }
    }
}


