package com.sss.monikaapps.feature.home.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomRoundedIconContainer
import com.sss.monikaapps.common.theme.BodyBitterSemiBold
import com.sss.monikaapps.common.theme.BodyPopSemiBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.common.theme.TitlePopBold

@Composable
fun HomeHeader(userName: String, nameDepo: String, userRole: String) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            CustomRoundedIconContainer(
//                onClick = {},
//            ) {
//                Image(
//                    painter = painterResource(R.drawable.icon_notification),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(Dimens.ExtraSmallMargin)
//                )
//            }
//            Spacer(modifier = Modifier.weight(1f))
//            CustomRoundedIconContainer(
//                onClick = {},
//            ) {
//                Image(
//                    painter = painterResource(R.drawable.icon_setting),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(Dimens.ExtraSmallMargin)
//                )
//            }
//        }

        Spacer(modifier = Modifier.height(Dimens.ExtraLargeMargin))

        Text("Hello!", style = BodyPopSemiBold.copy(fontSize = Dimens.ExtraLargeFont))
        Text(userName, style = TitlePopBold.copy(color = Primary, fontSize = Dimens.ExtraLargeFont))
        Text("$nameDepo - $userRole", style = BodyBitterSemiBold.copy(fontSize = Dimens.LargeFont))
    }
}