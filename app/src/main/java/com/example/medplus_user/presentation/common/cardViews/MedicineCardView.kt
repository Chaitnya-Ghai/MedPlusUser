package com.example.medplus_user.presentation.common.cardViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.medplus_user.domain.models.Medicines
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun MedicineCard(med: Medicines, isFullSpan: Boolean) {
    val painter = rememberAsyncImagePainter(model = med.medicineImg)
    val isImageLoading = painter.state is AsyncImagePainter.State.Loading

    val modifier = if (isFullSpan)
        Modifier.fillMaxWidth()
    else Modifier.width(185.dp)

    Card(
        modifier = modifier
            .height(186.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .placeholder(
                    visible = isImageLoading,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = Color.LightGray,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Image(
                painter = painter,
                contentDescription = "Medicine",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (painter.state is AsyncImagePainter.State.Error) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Image load failed",
                    tint = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

