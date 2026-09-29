package com.sss.monikaapps.feature.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens


@Composable
fun PhotoDetailScreen(
    urlPhoto: String,
    navController: NavController,
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        offset += panChange
    }
    val imageLoader = rememberImageLoader()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
            .padding(top = Dimens.MediumMargin)
    ) {
        CustomTopBar(
            title = "Detail Foto",
            onBackClick = { navController.popBackStack() },
        )

        AsyncImage(
            model = urlPhoto,
            imageLoader = imageLoader,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .transformable(state)
        )
    }
}
