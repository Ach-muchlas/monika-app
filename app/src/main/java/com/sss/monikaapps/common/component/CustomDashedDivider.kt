package com.sss.monikaapps.common.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.Dimens


@Composable
fun DashedDivider(
    showText: Boolean = true,
    text: String = "",
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = color,
    thickness: Dp = 2.dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 6.dp,
    textPadding: Dp = 12.dp,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DashedLine(
            modifier = Modifier.weight(1f),
            color = color,
            thickness = thickness,
            dashLength = dashLength,
            gapLength = gapLength
        )

        if (showText) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = textPadding),
                color = textColor,
                style = BodyPopBold,
                fontSize = Dimens.LargeFont
            )
        }

        DashedLine(
            modifier = Modifier.weight(1f),
            color = color,
            thickness = thickness,
            dashLength = dashLength,
            gapLength = gapLength
        )
    }
}

@Composable
private fun DashedLine(
    modifier: Modifier,
    color: Color,
    thickness: Dp,
    dashLength: Dp,
    gapLength: Dp,
) {
    Canvas(
        modifier = modifier.height(thickness)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = thickness.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(
                    dashLength.toPx(),
                    gapLength.toPx()
                )
            )
        )
    }
}
