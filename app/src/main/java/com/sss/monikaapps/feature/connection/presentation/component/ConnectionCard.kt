package com.sss.monikaapps.feature.connection.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomCheckbox
import com.sss.monikaapps.common.component.CustomDropdownTextField
import com.sss.monikaapps.common.component.CustomOutlineButton
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTextField
import com.sss.monikaapps.common.data.UrlModel
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary

@Composable
fun ConnectionCard(
    isFirstTime: Boolean = false,
    imei: String,
    onCopy: () -> Unit,
    systemOperation: String,
    versionApps: String,
    listUrl: List<UrlModel>,
    selectedUrl: UrlModel?,
    onUrlSelected: (UrlModel) -> Unit,
    onSave: (String) -> Unit,
    onClickToLogin: () -> Unit,
) {

    var ipA by remember { mutableStateOf("") }
    var ipB by remember { mutableStateOf("") }
    var ipC by remember { mutableStateOf("") }
    var ipD by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }

    val focusIpA = remember { FocusRequester() }
    val focusIpB = remember { FocusRequester() }
    val focusIpC = remember { FocusRequester() }
    val focusIpD = remember { FocusRequester() }
    val focusPort = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SmallMargin),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(Dimens.MediumMargin))
        Text(
            text = stringResource(R.string.text_title_imei),
            style = BodyPopBold,
            modifier = Modifier.fillMaxWidth()
        )

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
                        tint = Primary
                    )
                }
            })

        Spacer(Modifier.height(Dimens.MediumMargin))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoPill(
                title = stringResource(R.string.text_title_system_operation),
                value = systemOperation,
                modifier = Modifier.weight(1.5f)
            )

            InfoPill(
                title = stringResource(R.string.text_title_version_apps),
                value = versionApps,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(Dimens.MediumMargin))
        Text(
            text = stringResource(R.string.text_server_name),
            style = BodyPopBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomCheckbox(
                checked = checked, onCheckedChange = { checked = it })

            Spacer(modifier = Modifier.width(Dimens.ExtraSmallMargin))

            CustomDropdownTextField(
                label = "Pilih Koneksi",
                items = listUrl,
                selectedItem = selectedUrl,
                enabled = checked,
                onItemSelected = {
                    onUrlSelected(it)
                },
                itemText = { it.urlName })
        }

        if (selectedUrl?.urlName == "Lainnya") {

            Spacer(Modifier.height(Dimens.MediumMargin))

            Row {
                IpTextField(
                    value = ipA,
                    hint = "202",
                    focusRequester = focusIpA,
                    nextFocus = focusIpB,
                    onValueChangeState = { ipA = it },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(Dimens.ExtraSmallMargin))

                IpTextField(
                    value = ipB,
                    hint = "1",
                    focusRequester = focusIpB,
                    nextFocus = focusIpC,
                    onValueChangeState = { ipB = it },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(Dimens.ExtraSmallMargin))

                IpTextField(
                    value = ipC,
                    hint = "133",
                    focusRequester = focusIpC,
                    nextFocus = focusIpD,
                    onValueChangeState = { ipC = it },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(Dimens.ExtraSmallMargin))

                IpTextField(
                    value = ipD,
                    hint = "202",
                    focusRequester = focusIpD,
                    nextFocus = focusPort,
                    onValueChangeState = { ipD = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(Dimens.SmallMargin))

            CustomTextField(
                value = port,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                hint = "202",
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        port = newValue
                    }
                },
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        }

        Spacer(Modifier.height(Dimens.MediumMargin))
        CustomPrimaryButton(text = stringResource(R.string.text_save)) {
            val finalUrl = if (selectedUrl?.urlName == "Lainnya") {
                "http://$ipA.$ipB.$ipC.$ipD:$port"
            } else {
                selectedUrl?.urlValue
            }

            if (finalUrl?.isNotBlank() == true) {
                onSave(finalUrl)
            }
        }
        if (isFirstTime) {
            Spacer(Modifier.height(Dimens.MediumMargin))
            CustomOutlineButton(
                text = stringResource(R.string.text_to_login),
                onClick = onClickToLogin
            )
        }
        Spacer(Modifier.height(Dimens.MediumMargin))

    }
}
