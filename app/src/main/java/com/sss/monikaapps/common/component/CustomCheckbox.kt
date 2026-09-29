package com.sss.monikaapps.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(Dimens.ExtraSmallCornerRadius)

    Box(
        modifier = modifier
            .size(38.dp)
            .clickable { onCheckedChange(!checked) }
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                drawRoundRect(
                    color = Primary,
                    size = size,
                    cornerRadius = CornerRadius(6.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
            }
            .clip(shape)
            .background(if (checked) Primary else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                painter = painterResource(R.drawable.ic_checked),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
