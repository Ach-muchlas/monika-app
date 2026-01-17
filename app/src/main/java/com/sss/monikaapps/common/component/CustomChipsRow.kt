package com.sss.monikaapps.common.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.theme.BodyPopMedium
import com.sss.monikaapps.common.theme.BorderColor
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.common.theme.TextInactive

@Composable
fun CustomFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) Primary else Color.White,
        label = "bg"
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) Color.White else TextInactive,
        label = "content"
    )

    val elevation by animateDpAsState(
        targetValue = if (selected) 4.dp else 0.dp,
        label = "elevation"
    )

    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else 0.97f,
        label = "scale"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                shadowElevation = elevation.toPx()
                shape = RoundedCornerShape(18.dp)
                clip = true
            }
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = if (selected) Color.Transparent else BorderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 25.dp, vertical = Dimens.SmallMargin),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            style = BodyPopMedium,
        )
    }
}

