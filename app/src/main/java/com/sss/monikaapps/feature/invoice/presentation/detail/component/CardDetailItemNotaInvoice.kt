package com.sss.monikaapps.feature.invoice.presentation.detail.component

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.theme.BodyPopRegular
import com.sss.monikaapps.common.theme.BodyPopSemiBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray
import com.sss.monikaapps.common.theme.LightRed
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.feature.activity.data.response.PhotoItem
import com.sss.monikaapps.feature.photo.CustomPhoto

@Composable
fun CardDetailItemNotaInvoice(
    idInvoice: String,
    nomorNota: String,
    outstanding: String,
    statusId: Int,
    status: String,
    dateNote: String,
    dueDate: String,
    textReasonOrTotal: String,
    syncStatus : Int,
    lisPhoto: List<PhotoItem>?,
    clickPayment: (idInvoice: String, nomorNota: String) -> Unit,
    clickDetailPhoto: (url: String) -> Unit,
) {
    val color = when (syncStatus) {
        1 -> LightRed
        2 -> Primary
        else -> Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.MediumCornerRadius),
        elevation = CardDefaults.cardElevation(Dimens.ExtraSmallCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { clickPayment.invoke(idInvoice, nomorNota) }
    ) {
        val icon = if (statusId == 2) R.drawable.icon_reason else R.drawable.icon_paid

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
                        nomorNota,
                        style = BodyPopSemiBold
                    )
                    Text(
                        status,
                        style = BodyPopSemiBold.copy(color = Primary, fontSize = Dimens.LargeFont)
                    )
                }

                Text(outstanding, style = BodyPopSemiBold.copy(color = Primary))

                Spacer(modifier = Modifier.height(Dimens.SmallMargin))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_date_order),
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(R.string.text_date_note)
                    )
                    Spacer(modifier = Modifier.width(Dimens.SmallMargin))
                    Text(dateNote, style = BodyPopRegular.copy(fontSize = Dimens.MediumFont))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_due_date),
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(R.string.text_due_date)
                    )
                    Spacer(modifier = Modifier.width(Dimens.SmallMargin))
                    Text(dueDate, style = BodyPopRegular.copy(fontSize = Dimens.MediumFont))
                }

                if (statusId != 0) {
                    Spacer(modifier = Modifier.height(Dimens.ExtraSmallMargin))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(icon),
                            modifier = Modifier.size(30.dp),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.width(Dimens.SmallMargin))

                        Text(
                            textReasonOrTotal,
                            style = BodyPopSemiBold.copy(fontSize = Dimens.MediumFont)
                        )
                    }

                    if (!lisPhoto.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(Dimens.SmallMargin))
                        LazyRow(
                            modifier = Modifier.height(90.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(lisPhoto) { photo ->
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
    }
}