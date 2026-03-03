package com.sss.monikaapps.feature.invoice.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.theme.BodyBitterMedium
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.DarkRed
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.common.theme.TitlePopBold
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity

@Composable
fun CardListInvoice(
    data: CustomerInvoiceEntity,
    onClick: () -> Unit,
) {
    val color = when (data.syncStatus) {
        1, 3 -> DarkRed
        2, 4 -> Primary
        else -> Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(Dimens.MediumCornerRadius),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(2.dp, color),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = data.customerId,
                        style = TitlePopBold.copy(fontSize = Dimens.LargeFont, color = Primary)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = data.customerName, style = BodyPopBold, overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = data.address,
                    style = BodyBitterMedium.copy(color = Gray, fontSize = Dimens.MediumFont),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = when (data.syncStatus) {
                    1, 2 -> "Check In"
                    3, 4 -> "Check Out"
                    else -> ""
                }, style = BodyPopBold.copy(color = Gray, fontSize = Dimens.MediumFont)
            )
        }
    }
}