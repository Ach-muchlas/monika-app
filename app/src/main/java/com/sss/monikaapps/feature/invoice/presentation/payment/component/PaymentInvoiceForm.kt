package com.sss.monikaapps.feature.invoice.presentation.payment.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomCheckbox
import com.sss.monikaapps.common.component.CustomDropdownTextField
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTextField
import com.sss.monikaapps.common.formatter.FormatterCurrency
import com.sss.monikaapps.common.formatter.FormatterCurrency.cleanCurrency
import com.sss.monikaapps.common.helper.ThousandsSeparatorTransformationHelper
import com.sss.monikaapps.common.theme.BodyBitterBold
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.BodyPopRegular
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity
import com.sss.monikaapps.feature.photo.CustomMultiPhotoCard

@Composable
fun PaymentInvoiceForm(
    customerData: String,
    nomorNota: String,
    totalOutstanding: String,
    totalPaid: String,
    statusPaid: Int,
    reasons: List<ReasonEntity>?,
    selectedReason: ReasonEntity?,
    onPaidChange: (String) -> Unit,
    onStatusPaidChange: (Int) -> Unit,
    onReasonChange: (ReasonEntity) -> Unit,
    photos: List<String>,
    onAddPhoto: () -> Unit,
    onDeletePhoto: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var totalPaidError by remember { mutableStateOf<String?>(null) }
    var reasonError by remember { mutableStateOf<String?>(null) }
    var photoError by remember { mutableStateOf<String?>(null) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SmallMargin),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(Dimens.ExtraSmallMargin))

        Text(
            text = stringResource(R.string.text_form_payment_invoice),
            style = BodyBitterBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraLargeMargin))


        Text(
            text = stringResource(R.string.text_customer_data),
            style = BodyPopBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = customerData,
            onValueChange = {},
            hint = stringResource(R.string.text_customer_data),
            readOnly = true
        )

        Spacer(Modifier.height(Dimens.MediumMargin))

        Text(
            text = stringResource(R.string.text_nomor_nota),
            style = BodyPopBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = nomorNota,
            onValueChange = {},
            hint = stringResource(R.string.text_nomor_nota),
            readOnly = true
        )

        Spacer(Modifier.height(Dimens.MediumMargin))

        Text(
            text = stringResource(R.string.text_outstanding_nota),
            style = BodyPopBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = totalOutstanding,
            onValueChange = {},
            hint = stringResource(R.string.text_outstanding_nota),
            readOnly = true
        )

        Spacer(Modifier.height(Dimens.MediumMargin))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onStatusPaidChange(1) }
            ) {
                CustomCheckbox(
                    checked = statusPaid == 1,
                    onCheckedChange = { if (it) onStatusPaidChange(1) }
                )
                Spacer(modifier = Modifier.width(Dimens.SmallMargin))
                Text(text = "Bayar", style = BodyPopBold)
            }

            Spacer(modifier = Modifier.width(Dimens.LargeMargin))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onStatusPaidChange(2) }
            ) {
                CustomCheckbox(
                    checked = statusPaid == 2,
                    onCheckedChange = { if (it) onStatusPaidChange(2) }
                )
                Spacer(modifier = Modifier.width(Dimens.SmallMargin))
                Text(text = "Tidak Bayar", style = BodyPopBold)
            }
        }

        Spacer(Modifier.height(Dimens.MediumMargin))

        if (statusPaid == 1) {
            // Tampilkan Input Nominal Bayar
            Text(
                text = stringResource(R.string.text_total_paid),
                style = BodyPopBold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = totalPaid,
                onValueChange = {
                    totalPaidError = null
                    onPaidChange(it)
                },
                hint = "Masukan total bayar",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = ThousandsSeparatorTransformationHelper(),
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
            totalPaidError?.let { error ->
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
        } else if (statusPaid == 2) {

            Text(
                text = "Alasan Tidak Bayar",
                style = BodyPopBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

            CustomDropdownTextField(
                label = "Masukan alasan tidak bayar",
                items = reasons ?: emptyList(),
                selectedItem = selectedReason,
                onItemSelected = {
                    reasonError == null
                    onReasonChange(it)
                },
                itemText = { it.descReason }
            )

            reasonError?.let { error ->
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

            Text(
                text = stringResource(R.string.text_evidence_reason),
                style = BodyPopBold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

            // Grid foto
            CustomMultiPhotoCard(
                title = "Masukan bukti foto", photos = photos,
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

        }

        Spacer(Modifier.height(Dimens.ExtraExtraLargeMargin))

      CustomPrimaryButton(
        text = stringResource(R.string.text_save),
        onClick = {
            var hasError = false

            // Bersihkan input nominal menjadi angka Long agar aman dibandingkan
            val paidAmount = cleanCurrency(totalPaid)
            val outstandingAmount = cleanCurrency(totalOutstanding)

            if (statusPaid == 1) {
                if (totalPaid.isBlank()) {
                    totalPaidError = "Uang yang dibayarkan tidak boleh kosong"
                    hasError = true
                } else if (paidAmount <= 0) {
                    totalPaidError = "Uang yang dibayarkan tidak boleh 0"
                    hasError = true
                } else if (paidAmount > outstandingAmount) {
                    totalPaidError = "Uang yang dibayarkan tidak boleh lebih dari total tagihan"
                    hasError = true
                }
            }

            if (statusPaid == 2) {
                // Cek apakah alasan sudah dipilih (bukan string-nya saja, tapi objeknya)
                if (selectedReason == null || selectedReason.descReason.isNullOrEmpty()) {
                    reasonError = "Alasan tidak bayar tidak boleh kosong"
                    hasError = true
                }

                if (photos.isEmpty()) {
                    photoError = "Minimal 1 foto harus ditambahkan sebagai bukti"
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
