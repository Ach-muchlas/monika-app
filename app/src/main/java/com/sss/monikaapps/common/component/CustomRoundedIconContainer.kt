package com.sss.monikaapps.common.component

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.theme.Dimens


@Composable
fun CustomRoundedIconContainer(
    modifier: Modifier = Modifier,
    size: Dp = 42.dp,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    CustomPressableCard(
        modifier = modifier.size(size),
        shape = RoundedCornerShape(Dimens.ExtraExtraLargeMargin),
        elevation = 8.dp,
        backgroundBrush = Brush.linearGradient(
            listOf(Color.White, Color(0xFFF2F2F2))
        ),
        onClick = onClick
    ) {
        content()
//        Image(
//            painter = painterResource(icon),
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//        )
    }
}
