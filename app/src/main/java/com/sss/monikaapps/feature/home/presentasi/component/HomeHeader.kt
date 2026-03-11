package com.sss.monikaapps.feature.home.presentasi.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.theme.BodyBitterMedium
import com.sss.monikaapps.common.theme.BodyBitterSemiBold
import com.sss.monikaapps.common.theme.BodyPopSemiBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.common.theme.TitlePopBold

@Composable
fun HomeHeader(userName: String, nameDepo: String, userRole: String, downloadDate: String) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(Dimens.ExtraLargeMargin))

        Text("Hello!", style = BodyPopSemiBold.copy(fontSize = Dimens.ExtraLargeFont))
        Text(userName, style = TitlePopBold.copy(color = Primary, fontSize = Dimens.ExtraLargeFont))
        Text("$nameDepo - $userRole", style = BodyBitterSemiBold.copy(fontSize = Dimens.LargeFont))
        Spacer(modifier = Modifier.height(3.dp))
        Text(downloadDate, style = BodyBitterMedium.copy(color = Gray, fontSize = Dimens.MediumFont))
    }
}