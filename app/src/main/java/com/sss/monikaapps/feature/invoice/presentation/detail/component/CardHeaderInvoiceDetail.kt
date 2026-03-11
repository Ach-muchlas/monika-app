package com.sss.monikaapps.feature.invoice.presentation.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.formatter.FormatterTime.formatTimeOnly
import com.sss.monikaapps.common.theme.BodyBitterMedium
import com.sss.monikaapps.common.theme.BodyPopRegular
import com.sss.monikaapps.common.theme.BodyPopSemiBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.common.theme.TitlePopBold
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity

@Composable
fun CardHeaderInvoiceDetail(
    data : CustomerInvoiceEntity
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.MediumCornerRadius),
        elevation = CardDefaults.cardElevation(Dimens.ExtraSmallCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            modifier = Modifier.padding(Dimens.MediumMargin)
        ) {
            Text(data.customerId, style = TitlePopBold.copy(color = Primary))
            Spacer(modifier = Modifier.height(Dimens.ExtraSmallMargin))
            Text(data.customerName, style = BodyPopSemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(data.address, style = BodyBitterMedium.copy(fontSize = Dimens.MediumFont, color = Gray))
            Spacer(modifier = Modifier.height(Dimens.LargeMargin))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_coordinates),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Icon profile"
                )
                Spacer(modifier = Modifier.width(Dimens.SmallMargin))
                Text("${data.gpsLatCustomer} - ${data.gpsLngCustomer}", style = BodyPopRegular.copy(fontSize = 14.sp))
            }
        }
    }
}