package com.example.medplus_user.presentation.common.cardViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.medplus_user.R
import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.presentation.CategoryScreen
import com.example.medplus_user.presentation.ui.rememberCustomImageLoader
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

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
        val safeUrl = category.imageUrl.replace(" ", "%20")
        val imageLoader = rememberCustomImageLoader()

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(safeUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .error(R.drawable.error)
                .build(),
            imageLoader = imageLoader
        )

        val isImageLoading = painter.state is AsyncImagePainter.State.Loading

        Box(
            modifier = Modifier
                .fillMaxSize()
                .placeholder(
                    visible = isImageLoading,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = Color.LightGray,
                )
        ) {
            Image(
                painter = painter,
                contentDescription = category.categoryName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}