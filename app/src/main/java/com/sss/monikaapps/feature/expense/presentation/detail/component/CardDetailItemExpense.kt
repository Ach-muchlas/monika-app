package com.sss.monikaapps.feature.expense.presentation.detail.component

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.component.CustomFloatingActionButton
import com.sss.monikaapps.common.component.CustomPhoto
import com.sss.monikaapps.common.formatter.FormatterCurrency.formatCurrency
import com.sss.monikaapps.common.formatter.FormatterCurrency.formatCurrencyWithoutRp
import com.sss.monikaapps.common.theme.BodyBitterRegular
import com.sss.monikaapps.common.theme.BodyPopSemiBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.feature.activity.data.response.PhotoItem
import com.sss.monikaapps.feature.expense.data.response.DetailItemExpense

@Composable
fun CardDetailItemExpense(
    dataItem: DetailItemExpense,
    status: String,
    lisPhoto: List<PhotoItem>?,
    onClickEdited: (trno: String, idDetail: String, netAmount: String, note: String, initKm: String, finalKm: String) -> Unit,
    onClickDeleted: (trno: String, idDetail: String) -> Unit,
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
                .background(Primary)
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
                        formatCurrency(amount = dataItem.netAmountDetail?.toLong() ?: 0),
                        style = BodyPopSemiBold
                    )
                    Text(
                        dataItem.name.toString(),
                        style = BodyPopSemiBold.copy(color = Primary, fontSize = Dimens.LargeFont)
                    )
                }

                if (dataItem.initialKilometer != "0") {
                    Text(
                        "${formatCurrencyWithoutRp(dataItem.initialKilometer?.toLong() ?: 0)} - ${
                            formatCurrencyWithoutRp(dataItem.finalKilometer?.toLong() ?: 0)
                        }", style = BodyBitterRegular
                    )
                }
                Text(dataItem.note ?: "-", style = BodyBitterRegular.copy(color = Gray))
                Spacer(modifier = Modifier.height(Dimens.SmallMargin))

                LazyRow(
                    modifier = Modifier.height(90.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(lisPhoto.orEmpty()) { photo ->
                        CustomPhoto(
                            imageUrl = photo.path.toString(),
                            modifier = Modifier.size(90.dp),
                            onClick = {})
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.SmallMargin))

                if (status == "0") {
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        CustomFloatingActionButton(
                            modifier = Modifier.size(50.dp),
                            imageVector = Icons.Default.Edit,
                            onClick = {
                                onClickEdited(
                                    dataItem.trnoTransaction.toString(),
                                    dataItem.idExpanse.toString(),
                                    dataItem.netAmountDetail.toString(),
                                    if (dataItem.note.isNullOrEmpty()) "-" else dataItem.note,
                                    dataItem.initialKilometer ?: "0",
                                    dataItem.finalKilometer ?: "0"
                                )
                            })
                        Spacer(modifier = Modifier.width(Dimens.ExtraSmallMargin))
                        CustomFloatingActionButton(
                            modifier = Modifier.size(50.dp),
                            imageVector = Icons.Default.Delete,
                            gradientLight = Color(0xFFFF493A),
                            gradientMid = Color(0xFFD94646),
                            gradientDark = Color(0xFF8B2424),
                            onClick = {
                                onClickDeleted(
                                    dataItem.trnoTransaction.toString(),
                                    dataItem.idExpanse.toString()
                                )
                            })
                    }
                }
            }
        }
    }
}