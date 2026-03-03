package com.sss.monikaapps.feature.home.presentasi.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sss.monikaapps.common.exention.UiExtension.singleClick
import com.sss.monikaapps.common.theme.BodyBitterMedium
import com.sss.monikaapps.common.theme.CardWhite
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.common.theme.TitlePopBold
import com.sss.monikaapps.feature.home.data.model.HomeMenuItem
import java.time.format.TextStyle

@Composable
fun HomeMenuGrid(menuItems: List<HomeMenuItem>, onMenuClick: (Int) -> Unit) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumMargin),
        horizontalArrangement = Arrangement.spacedBy(Dimens.MediumMargin),
        contentPadding = PaddingValues(
            top = Dimens.MediumMargin,
            bottom = Dimens.MediumMargin
        ),
        modifier = Modifier.fillMaxSize()
    ) {
        items(menuItems) { item ->
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .singleClick { onMenuClick(item.idMenu) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CardWhite)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {

                    Image(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.title,
                        modifier = Modifier.size(90.dp)
                    )
                    Column {
                        ResponsiveTitleText(
                            text = item.title,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.description,
                            style = BodyBitterMedium.copy(fontSize = 12.sp),
                            color = Color.Gray,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun ResponsiveTitleText(
    text: String,
    modifier: Modifier = Modifier,
    maxFontSize: TextUnit = 25.sp,
    minFontSize: TextUnit = 20.sp,
) {
    var fontSize by remember { mutableStateOf(maxFontSize) }
    var isReady by remember { mutableStateOf(false) }

    Text(
        text = text,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Clip,
        style = TitlePopBold.copy(fontSize = fontSize, color = Primary),
        modifier = modifier.drawWithContent {
            if (isReady) drawContent()
        },
        onTextLayout = { result ->
            if (result.didOverflowWidth && fontSize > minFontSize) {
                fontSize *= 0.9f
            } else {
                isReady = true
            }
        }
    )
}
