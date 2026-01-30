package com.sss.monikaapps.feature.activity.ui.component

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
import com.sss.monikaapps.common.theme.BodyPopMedium
import com.sss.monikaapps.common.theme.BodyPopRegular
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.common.theme.TitlePopBold


@Composable
fun CardHeaderActivityDetail(
    employeeName: String,
    title: String,
    subtitle: String,
    checkInTime: String,
    checkOutTime: String,
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
            Text(title, style = TitlePopBold.copy(color = Primary))
            Spacer(modifier = Modifier.height(Dimens.ExtraSmallMargin))
            Text(subtitle, style = BodyPopMedium)
            Spacer(modifier = Modifier.height(Dimens.LargeMargin))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_profile),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Icon profile"
                )
                Spacer(modifier = Modifier.width(Dimens.SmallMargin))
                Text(employeeName, style = BodyPopRegular.copy(fontSize = 14.sp))
            }
            Spacer(modifier = Modifier.height(Dimens.ExtraSmallMargin))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_clock),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Icon time"
                )
                Spacer(modifier = Modifier.width(Dimens.SmallMargin))
                Text(
                    "${formatTimeOnly(checkInTime)} - ${formatTimeOnly(checkOutTime)}",
                    style = BodyPopRegular.copy(fontSize = 14.sp)
                )
            }
        }
    }
}