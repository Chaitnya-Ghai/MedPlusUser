package com.example.medplus_user.presentation.common.cardViews

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.medplus_user.domain.models.Shopkeeper

@Composable
fun ShopkeeperCard(shopkeeper: Shopkeeper) {
    Log.e("ShopkeeperCard", "shopkeeper id inside card ${shopkeeper.id}")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 12.dp)
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
                Text("~ ${ shopkeeper.ownerName}", color = Color.LightGray, fontSize = 10.sp)
            }
        }
    }
}
