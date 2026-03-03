package com.sss.monikaapps.feature.photo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sss.monikaapps.R

@Composable
fun CustomPhoto(
    imageUrl: String,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    height: Dp = 120.dp,
    onClick: () -> Unit,
) {

    val imageLoader = rememberImageLoader()

    Card(
        modifier = modifier
            .height(height)
            .clickable { onClick() },
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            imageLoader = imageLoader,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(R.drawable.example_image)
        )
    }
}