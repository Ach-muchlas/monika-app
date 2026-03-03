package com.sss.monikaapps.feature.login.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomOutlineButton
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.DashedDivider
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.BodyPopMedium
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.common.theme.TitleBitterBold

@Composable
fun LoginHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        Image(
            painter = painterResource(R.drawable.logo_monika_apps),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = Dimens.ExtraLargeMargin),
            contentScale = ContentScale.Fit
        )
        Text(
            text = stringResource(R.string.text_full_name_apps),
            style = TitleBitterBold.copy(fontSize = 23.sp),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(30.dp))
    }
}

@Composable
fun LoginForm(
    imei: String,
    employeeId: String,
    onEmployeeChange: (String) -> Unit,
    onCopyImei: () -> Unit,
    onLoginClick: () -> Unit,
    onLoginConnection: () -> Unit,
    focusManager: FocusManager,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.LargeMargin),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {
            Text("Welcome to ", style = BodyPopMedium.copy(fontSize = 25.sp))
            Text(
                "Monika",
                style = BodyPopBold.copy(color = Primary),
                fontSize = 25.sp
            )
        }

        Text(
            text = "Login now!",
            style = BodyPopMedium.copy(fontSize = 25.sp)
        )

        Spacer(Modifier.height(Dimens.ExtraLargeMargin))

        ImeiField(imei, onCopyImei)

        Spacer(Modifier.height(Dimens.MediumMargin))

        EmployeeField(employeeId, onEmployeeChange, focusManager)

        Spacer(Modifier.height(Dimens.ExtraExtraLargeMargin))

        CustomPrimaryButton(
            text = "Login",
            onClick = onLoginClick
        )

        Spacer(Modifier.height(Dimens.LargeMargin))

        DashedDivider(
            showText = true,
            text = "ATAU",
            color = Color.Black.copy(alpha = 0.35f),
            textColor = Primary,
            thickness = 3.dp,
            dashLength = 12.dp,
            gapLength = 8.dp
        )

        Spacer(Modifier.height(Dimens.LargeMargin))

        CustomOutlineButton("Konfigurasi Koneksi", onClick = onLoginConnection)
    }
}

@Composable
fun BoxScope.LoginFooter(versionApps: String) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.text_copy_right),
            style = BodyPopMedium.copy(fontSize = 12.sp, color = Color.Gray)
        )

        Text(
            text = "Version $versionApps",
            style = BodyPopMedium.copy(fontSize = 12.sp, color = Color.Gray)
        )
    }
}