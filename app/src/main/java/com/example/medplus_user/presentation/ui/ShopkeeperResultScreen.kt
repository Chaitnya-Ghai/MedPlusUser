package com.example.medplus_user.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.medplus_user.domain.models.Shopkeeper
import com.example.medplus_user.presentation.MedicineScreen
import com.example.medplus_user.presentation.viewModel.MainViewModel
import kotlin.String

@Composable
fun ShopkeeperShopScreen(arg: String?, navController: NavHostController, viewModel: MainViewModel){
//    val shopkeepers = viewModel.shopkeepers.collectAsState()
//    LaunchedEffect(arg) {
//        viewModel.fetchShopkeepers(medicineId = arg.toString())
//    }
//    Column(Modifier.fillMaxSize(.95f)) {
//        val query = remember { mutableStateOf("") }
//        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.Center) { SearchField(query = query) }
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(1),
//            modifier = Modifier
//                .fillMaxHeight(1f) // this bounds the height
//                .padding(5.dp),
//            userScrollEnabled = true
//        ) {
//            if (query.value.isBlank()) {
//                items(shopkeepers.value) { shops ->
//                    ShopCard(navController = navController ,shops , arg = arg)
//                }
//            }
//            else{
//                items(shopkeepers.value.filter { it.shopName.toString().contains(query.value) }) { shops ->
//                    ShopCard(navController = navController ,shops , arg = arg)
//                }
//            }
//        }
//    }
}


@Composable
fun ShopCard(navController:NavController ,item: Shopkeeper , arg: String?){

    Card(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        onClick = {
            val index = item.inventory.indexOfFirst { it.medicineId == arg.toString() }
            navController.navigate(MedicineScreen(id = arg.toString() , shopMedicinePrice = item.inventory[index].shopMedicinePrice  ))
        }
    ){
        Row(Modifier.fillMaxWidth(1f)) {
            Card (
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp).padding(start = 12.dp , top = 4.dp , end = 4.dp , bottom = 4.dp) ,
                colors =  CardDefaults.cardColors(
                    containerColor = Color.White,
                )

            ){
                Image(
                    painter = rememberAsyncImagePainter(model = item.shopImageUrl),
                    contentDescription = "Image of ${item.shopName}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)) {
                Text(
                    text = item.shopName.toString(),
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
                Text(
                    text = item.address.toString(),
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