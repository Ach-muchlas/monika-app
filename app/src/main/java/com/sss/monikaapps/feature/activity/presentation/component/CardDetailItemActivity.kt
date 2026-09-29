package com.sss.monikaapps.feature.activity.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.theme.BodyBitterRegular
import com.sss.monikaapps.common.theme.BodyPopSemiBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.feature.activity.data.response.PhotoItem
import com.sss.monikaapps.feature.photo.CustomPhoto

@Composable
fun CardDetailItemActivity(
    title: String,
    dateTime: String,
    latitude: String,
    longitude: String,
    color: Color = Primary,
    lisPhoto: List<PhotoItem>?,
    clickDetailPhoto: (url: String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.MediumCornerRadius),
        elevation = CardDefaults.cardElevation(Dimens.ExtraSmallCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
        ) {
            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(Dimens.MediumMargin)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Text(
                        com.sss.monikaapps.common.formatter.FormatterDate.formatDateTimeToDisplayDateTime(
                            dateTime
                        ),
                        style = BodyPopSemiBold
                    )
                    Text(
                        title,
                        style = BodyPopSemiBold.copy(color = Primary, fontSize = Dimens.LargeFont)
                    )
                }

                Text("$latitude - $longitude", style = BodyBitterRegular)

                Spacer(modifier = Modifier.height(Dimens.SmallMargin))

                LazyRow(
                    modifier = Modifier.height(90.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(lisPhoto.orEmpty()) { photo ->
                        CustomPhoto(
                            imageUrl = photo.path.toString(),
                            modifier = Modifier.size(90.dp),
                            onClick = { clickDetailPhoto(photo.path.toString()) }
                        )
                    }
                }
            }
        }
    }
}