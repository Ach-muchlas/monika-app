package com.sss.monikaapps.feature.login.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
@Composable
fun Floating3DCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 30.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .padding(bottom = 24.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 18.dp)
                .height(IntrinsicSize.Min)
                .background(
                    color = Color.Black.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .blur(32.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(cornerRadius),
                    ambientColor = Color.Black.copy(alpha = 0.25f),
                    spotColor = Color.Black.copy(alpha = 0.40f)
                )
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(cornerRadius)
                )

                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(cornerRadius)
                )
        ) {

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.9f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(cornerRadius)
                    )
            )

            content()
        }
    }
}
