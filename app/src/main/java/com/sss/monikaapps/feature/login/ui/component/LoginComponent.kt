package com.sss.monikaapps.feature.login.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomTextField
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary

@Composable
fun ImeiField(
    imei: String,
    onCopy: () -> Unit,
) {
    Text("IMEI", style = BodyPopBold, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

    CustomTextField(
        value = imei,
        onValueChange = {},
        hint = stringResource(R.string.text_input_imei),
        readOnly = true,
        textColor = Primary,
        trailingIcon = {
            IconButton(onClick = onCopy) {
                Icon(
                    painter = painterResource(R.drawable.icon_copy),
                    contentDescription = "Copy IMEI",
                    modifier = Modifier.size(30.dp),
                    tint = Primary
                )
            }
        }
    )
}

@Composable
fun EmployeeField(
    employeeId: String,
    onChange: (String) -> Unit,
    focusManager: FocusManager,
) {
    Text(
        text = "Employee ID",
        style = BodyPopBold,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

    CustomTextField(
        value = employeeId,
        onValueChange = onChange,
        hint = stringResource(R.string.text_input_employee_id),
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
}
