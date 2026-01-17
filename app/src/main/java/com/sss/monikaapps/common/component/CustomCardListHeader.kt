package com.sss.monikaapps.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.model.Status
import com.sss.monikaapps.common.theme.BodyBitterMedium
import com.sss.monikaapps.common.theme.BodyPopMedium
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray
import com.sss.monikaapps.common.theme.LightGray
import com.sss.monikaapps.common.theme.TitlePopBold


@Composable
fun CustomCardListHeader(
    title: String,
    subtitle: String,
    showNote: Boolean = false,
    note: String = "-",
    status: Status?,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.MediumCornerRadius),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = status?.bgColor ?: LightGray,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(status?.iconResource ?: R.drawable.icon_clock),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.width(Dimens.MediumMargin))

            // 📝 Text
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title, style = TitlePopBold.copy(fontSize = Dimens.LargeFont)
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    style = BodyPopMedium.copy(color = Gray, fontSize = Dimens.MediumFont),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )

                if (showNote) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = note,
                        style = BodyBitterMedium.copy(color = Gray, fontSize = Dimens.MediumFont)
                    )
                }
            }

            Text(
                text = status?.title ?: "-",
                style = BodyPopMedium.copy(color = Gray, fontSize = Dimens.MediumFont)
            )
        }
    }
}