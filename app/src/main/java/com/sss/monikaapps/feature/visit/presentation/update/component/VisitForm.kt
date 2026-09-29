package com.sss.monikaapps.feature.visit.presentation.update.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTextField
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.theme.BodyBitterBold
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.BodyPopRegular
import com.sss.monikaapps.common.theme.BodyPopSemiBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.feature.photo.CustomMultiPhotoCard

@Composable
fun VisitForm(
    typeVisit: String,
    descVisit: String,
    onDescChange: (String) -> Unit,
    latitude: String,
    onLatChange: (String) -> Unit,
    longitude: String,
    onLngChange: (String) -> Unit,
    photos: List<String>,
    onRefreshLatLong: () -> Unit,
    onAddPhoto: () -> Unit,
    onDeletePhoto: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    val isCheckIn = typeVisit == CHECK_IN
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var photoError by remember { mutableStateOf<String?>(null) }
    var descError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SmallMargin),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(Dimens.ExtraSmallMargin))

        Text(
            text = if (isCheckIn) stringResource(R.string.text_form_check_in_visit) else stringResource(
                R.string.text_form_check_out_visit
            ), style = BodyBitterBold, modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraLargeMargin))

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.text_coordinates),
                style = BodyPopBold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )

            Text(
                text = "Refresh",
                style = BodyPopSemiBold,
                color = Primary,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onRefreshLatLong() },
                textAlign = TextAlign.End
            )
        }


        Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomTextField(
                modifier = Modifier.weight(1f), value = latitude, onValueChange = {
                    onLatChange(it)
                }, hint = stringResource(R.string.text_lat), readOnly = true
            )
            Spacer(modifier = Modifier.width(Dimens.ExtraExtraSmallMargin))
            CustomTextField(
                modifier = Modifier.weight(1f), value = longitude, onValueChange = {
                    onLngChange(it)
                }, hint = stringResource(R.string.text_lng), readOnly = true
            )
        }
        Spacer(Modifier.height(Dimens.MediumMargin))

        if (isCheckIn) {
            Text(
                text = stringResource(R.string.text_description_visit),
                style = BodyPopBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

            CustomTextField(
                value = descVisit,
                onValueChange = {
                    descError = null
                    onDescChange(it)
                },
                modifier = Modifier.height(80.dp),
                isMultiline = true,
                hint = stringResource(R.string.text_input_note)
            )
            descError?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    style = BodyPopRegular,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }

            Spacer(Modifier.height(Dimens.MediumMargin))
        }

        Text(
            text = stringResource(R.string.text_evidence_visit),
            style = BodyPopBold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

        // Grid foto
        CustomMultiPhotoCard(
            title = "Masukan bukti foto kunjungan", photos = photos,
            onAddPhoto = {
                focusManager.clearFocus(force = true)
                keyboardController?.hide()
                photoError = null
                onAddPhoto()
            }, onDeletePhoto = onDeletePhoto
        )

        photoError?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                style = BodyPopRegular,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
            )
        }

        Spacer(Modifier.height(Dimens.ExtraExtraLargeMargin))

        CustomPrimaryButton(
            text = stringResource(R.string.text_save),
            onClick = {
                var hasError = false
                if (isCheckIn) {

                    if (descVisit.isBlank()) {
                        descError = "Catatan tidak boleh kosong"
                        hasError = true
                    }

                    if (photos.isEmpty()) {
                        photoError = "Minimal 1 foto harus ditambahkan"
                        hasError = true
                    }
                }
                if (!isCheckIn) {
                    if (photos.isEmpty()) {
                        photoError = "Minimal 1 foto harus ditambahkan"
                        hasError = true
                    }
                }

                if (!hasError) {
                    onSubmit()
                }
            })

        Spacer(Modifier.height(Dimens.LargeMargin))
    }
}
