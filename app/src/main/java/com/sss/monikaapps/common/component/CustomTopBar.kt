package com.sss.monikaapps.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.TitlePopSemiBold

@Composable
fun CustomTopBar(
    title: String,
    showRightIcon: Boolean = false,
    iconRight: Int = 0,
    iconSize: Int = 24,
    onBackClick: () -> Unit,
    onRightIconClick: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = Dimens.ExtraSmallMargin)
    ) {

        // Back Button
        CustomRoundedIconContainer(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = onBackClick,
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_back),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }


        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            style = TitlePopSemiBold.copy(fontSize = Dimens.ExtraLargeFont),
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Right Icon
        if (showRightIcon) {
            CustomRoundedIconContainer(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = { onRightIconClick?.invoke() },
            ) {
                Icon(
                    painter = painterResource(iconRight),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(iconSize.dp)
                )
            }
        }
    }
}
