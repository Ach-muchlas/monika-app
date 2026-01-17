package com.sss.monikaapps.common.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.PeachDark
import com.sss.monikaapps.common.theme.PeachLight
import com.sss.monikaapps.common.theme.PeachMid

@Composable
fun CustomFloatingActionButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        label = "fab_scale"
    )

    FloatingActionButton(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(Dimens.MediumCornerRadius),
        containerColor = Color.Transparent,
        contentColor = Color.White,
        modifier = modifier.scale(scale),
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 16.dp
        )
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(Dimens.MediumCornerRadius),
                    ambientColor = Color.Black.copy(alpha = 0.4f),
                    spotColor = Color.Black.copy(alpha = 0.5f)
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PeachLight,
                            PeachMid,
                            PeachDark
                        ),
                        center = Offset(0.35f, 0.25f),
                        radius = 280f
                    ),
                    shape = RoundedCornerShape(Dimens.MediumCornerRadius)
                ),
            contentAlignment = Alignment.Center
        ) {

            // Glossy highlight
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.18f),
                                Color.Transparent
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(300f, 300f)
                        ),
                        shape = RoundedCornerShape(Dimens.MediumCornerRadius)
                    )
            )

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = Color.White
            )
        }
    }
}
