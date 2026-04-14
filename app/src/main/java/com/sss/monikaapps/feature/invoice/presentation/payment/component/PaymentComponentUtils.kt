package com.sss.monikaapps.feature.invoice.presentation.payment.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.theme.BodyBitterBold
import com.sss.monikaapps.common.theme.BodyPopRegular


@Composable
 fun FormTitle() {
    Text(
        text = stringResource(R.string.text_form_payment_invoice),
        style = BodyBitterBold,
        modifier = Modifier.fillMaxWidth(),
    )
}


@Composable
fun ErrorText(message: String?) {
    if (message == null) return
    Text(
        text = message,
        color = Color.Red,
        style = BodyPopRegular,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
    )
}
