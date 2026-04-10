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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.sss.monikaapps.common.formatter.FormatterCurrency.cleanCurrency
import com.sss.monikaapps.common.formatter.FormatterDate
import com.sss.monikaapps.common.helper.ThousandsSeparatorTransformationHelper
import com.sss.monikaapps.common.theme.BodyBitterBold
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.BodyPopRegular
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray
import com.sss.monikaapps.feature.invoice.data.entity.BankReceiptEntity
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity
import com.sss.monikaapps.feature.photo.CustomMultiPhotoCard

// ─── PaymentInvoiceForm.kt ───────────────────────────────────────────────────

@Composable
fun PaymentInvoiceForm(
    customerData: String,
    nomorNota: String,
    totalOutstanding: String,
    totalNominalNota: String,
    totalPaid: String,
    statusPaid: Int,
    paymentMethod: Int,
    onDateChange: (String) -> Unit,
    reasons: List<ReasonEntity>?,
    selectedReason: ReasonEntity?,
    listBankReceipt: List<BankReceiptEntity>?,
    selectedBankReceipt: BankReceiptEntity?,
    onBankReceiptSelected: (BankReceiptEntity) -> Unit,
    onPaidChange: (String) -> Unit,
    onStatusPaidChange: (Int) -> Unit,
    onPaymentMethodChange: (Int) -> Unit,
    onReasonChange: (ReasonEntity) -> Unit,
    photos: List<String>,
    onAddPhoto: () -> Unit,
    onAddPhotoForReceipt: () -> Unit,
    onAddPhotoEvidenceTransfer: () -> Unit,
    onDeletePhoto: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDateError by remember { mutableStateOf(false) }

    var totalPaidError by remember { mutableStateOf<String?>(null) }
    var reasonError by remember { mutableStateOf<String?>(null) }
    var photoError by remember { mutableStateOf<String?>(null) }
    var photoErrorForReceipt by remember { mutableStateOf<String?>(null) }

    val formattedDateDisplay = remember(selectedDateMillis) {
        FormatterDate.formatTimestampToIndoDisplay(selectedDateMillis)
    }

    LaunchedEffect(selectedDateMillis) {
        selectedDateMillis?.let {
            onDateChange(FormatterDate.formatTimestampToDateString(it))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SmallMargin),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(Dimens.ExtraSmallMargin))

        FormTitle()

        Spacer(Modifier.height(Dimens.ExtraLargeMargin))

        ReadOnlyInfoSection(
            customerData = customerData,
            nomorNota = nomorNota,
            totalNominalNota = totalNominalNota,
            totalOutstanding = totalOutstanding,
        )

        Spacer(Modifier.height(Dimens.MediumMargin))

        PaymentStatusSelector(
            statusPaid = statusPaid,
            onStatusPaidChange = onStatusPaidChange,
        )

        Spacer(Modifier.height(Dimens.MediumMargin))

        when (statusPaid) {
            STATUS_BAYAR -> PaySection(
                totalPaid = totalPaid,
                totalPaidError = totalPaidError,
                methodPayment = paymentMethod,
                banks = listBankReceipt,
                photosEvidence = photos,
                selectedBank = selectedBankReceipt,
                onBankChange = onBankReceiptSelected,
                onAddPhoto = onAddPhotoEvidenceTransfer,
                onDeletePhoto = onDeletePhoto,
                onMethodPayment = {
                    onPaymentMethodChange(it)
                },
                onPaidChange = {
                    totalPaidError = null
                    onPaidChange(it)
                },
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            )

            STATUS_TIDAK_BAYAR -> NotPaySection(
                reasons = reasons,
                selectedReason = selectedReason,
                reasonError = reasonError,
                photoError = photoError,
                photos = photos,
                onReasonChange = {
                    reasonError = null
                    onReasonChange(it)
                },
                onAddPhoto = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                    photoError = null
                    onAddPhoto()
                },
                onDeletePhoto = onDeletePhoto,
            )

            STATUS_TANDA_TERIMA -> ReceiptSection(
                formattedDateDisplay = formattedDateDisplay,
                showDateError = showDateError,
                photoErrorForReceipt = photoErrorForReceipt,
                photos = photos,
                onDateFieldClick = {
                    showDateError = false
                    showDatePicker = true
                },
                onAddPhoto = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                    photoErrorForReceipt = null
                    onAddPhotoForReceipt()
                },
                onDeletePhoto = onDeletePhoto,
            )
        }

        Spacer(Modifier.height(Dimens.MediumMargin))

        CustomPrimaryButton(
            text = stringResource(R.string.text_save),
            onClick = {
                val hasError = validateForm(
                    statusPaid = statusPaid,
                    totalPaid = totalPaid,
                    totalOutstanding = totalOutstanding,
                    selectedReason = selectedReason,
                    photos = photos,
                    onTotalPaidError = { totalPaidError = it },
                    onReasonError = { reasonError = it },
                    onPhotoError = { photoError = it },
                )
                if (!hasError) onSubmit()
            },
        )

        Spacer(Modifier.height(Dimens.LargeMargin))
    }

    if (showDatePicker) {
        InvoiceDatePickerDialog(
            initialDateMillis = selectedDateMillis,
            onConfirm = { millis ->
                selectedDateMillis = millis
                showDateError = millis == null
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
        )
    }
}

// ─── Constants ───────────────────────────────────────────────────────────────

private const val STATUS_BAYAR = 1
private const val STATUS_TIDAK_BAYAR = 2
private const val STATUS_TANDA_TERIMA = 3

private const val PAYMENT_CASH = 1

private const val PAYMENT_TRANSFER = 2

// ─── Validation ──────────────────────────────────────────────────────────────

private fun validateForm(
    statusPaid: Int,
    totalPaid: String,
    totalOutstanding: String,
    selectedReason: ReasonEntity?,
    photos: List<String>,
    onTotalPaidError: (String) -> Unit,
    onReasonError: (String) -> Unit,
    onPhotoError: (String) -> Unit,
): Boolean {
    var hasError = false

    if (statusPaid == STATUS_BAYAR) {
        val paidAmount = cleanCurrency(totalPaid)
        val outstandingAmount = cleanCurrency(totalOutstanding)
        when {
            totalPaid.isBlank() -> {
                onTotalPaidError("Uang yang dibayarkan tidak boleh kosong")
                hasError = true
            }

            paidAmount <= 0 -> {
                onTotalPaidError("Uang yang dibayarkan tidak boleh 0")
                hasError = true
            }

            paidAmount > outstandingAmount -> {
                onTotalPaidError("Uang yang dibayarkan tidak boleh lebih dari total tagihan")
                hasError = true
            }
        }
    }

    if (statusPaid == STATUS_TIDAK_BAYAR) {
        if (selectedReason == null || selectedReason.descReason.isEmpty()) {
            onReasonError("Alasan tidak bayar tidak boleh kosong")
            hasError = true
        }
        if (photos.isEmpty()) {
            onPhotoError("Minimal 1 foto harus ditambahkan sebagai bukti")
            hasError = true
        }
    }

    return hasError
}

// ─── Sub-composables ─────────────────────────────────────────────────────────

@Composable
private fun FormTitle() {
    Text(
        text = stringResource(R.string.text_form_payment_invoice),
        style = BodyBitterBold,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ReadOnlyInfoSection(
    customerData: String,
    nomorNota: String,
    totalNominalNota: String,
    totalOutstanding: String,
) {
    val fields = listOf(
        stringResource(R.string.text_customer_data) to customerData,
        stringResource(R.string.text_nomor_nota) to nomorNota,
        stringResource(R.string.text_nominal_nota) to totalNominalNota,
        stringResource(R.string.text_outstanding_nota) to totalOutstanding,
    )
    fields.forEachIndexed { index, (label, value) ->
        LabeledReadOnlyField(label = label, value = value)
        if (index < fields.lastIndex) Spacer(Modifier.height(Dimens.MediumMargin))
    }
}

@Composable
private fun LabeledReadOnlyField(label: String, value: String) {
    Text(text = label, style = BodyPopBold, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
    CustomTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {},
        hint = label,
        readOnly = true,
    )
}

@Composable
private fun PaymentStatusSelector(
    statusPaid: Int,
    onStatusPaidChange: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        StatusCheckboxItem(
            label = "Bayar",
            checked = statusPaid == STATUS_BAYAR,
            onClick = { onStatusPaidChange(STATUS_BAYAR) },
        )
        Spacer(Modifier.width(Dimens.LargeMargin))
        StatusCheckboxItem(
            label = "Tidak Bayar",
            checked = statusPaid == STATUS_TIDAK_BAYAR,
            onClick = { onStatusPaidChange(STATUS_TIDAK_BAYAR) },
        )
    }

    Spacer(Modifier.height(Dimens.MediumMargin))

    StatusCheckboxItem(
        label = "Tanda Terima",
        checked = statusPaid == STATUS_TANDA_TERIMA,
        onClick = { onStatusPaidChange(STATUS_TANDA_TERIMA) },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun PaymentMethodSelector(
    methodPayment: Int,
    onMethodPayment: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        StatusCheckboxItem(
            label = "Cash",
            checked = methodPayment == PAYMENT_CASH,
            onClick = { onMethodPayment(PAYMENT_CASH) },
        )

        Spacer(Modifier.width(Dimens.LargeMargin))

        StatusCheckboxItem(
            label = "Transfer",
            checked = methodPayment == PAYMENT_TRANSFER,
            onClick = { onMethodPayment(PAYMENT_TRANSFER) },
        )
    }
}

@Composable
private fun StatusCheckboxItem(
    label: String,
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onClick() },
    ) {
        CustomCheckbox(
            checked = checked,
            onCheckedChange = { if (it) onClick() },
        )
        Spacer(Modifier.width(Dimens.SmallMargin))
        Text(text = label, style = BodyPopBold)
    }
}

@Composable
private fun PaySection(
    totalPaid: String,
    totalPaidError: String?,
    methodPayment: Int,
    banks: List<BankReceiptEntity>?,
    photosEvidence: List<String>,
    selectedBank: BankReceiptEntity?,
    onMethodPayment: (Int) -> Unit,
    onPaidChange: (String) -> Unit,
    onBankChange: (BankReceiptEntity) -> Unit,
    onAddPhoto: () -> Unit,
    onDeletePhoto: (String) -> Unit,
    onNext: () -> Unit,
) {
    Text(
        text = stringResource(R.string.text_method_payment),
        style = BodyPopBold,
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

    PaymentMethodSelector(methodPayment, onMethodPayment)

    Spacer(Modifier.height(Dimens.MediumMargin))

    if (methodPayment == PAYMENT_TRANSFER) {
        TransferSection(
            totalPaid = totalPaid,
            totalPaidError = totalPaidError,
            banks = banks,
            photos = photosEvidence,
            selectedBank = selectedBank,
            photoError = "",
            bankReceiptError = "",
            onPaidChange = onPaidChange,
            onBankChange = onBankChange,
            onAddPhoto = onAddPhoto,
            onDeletePhoto = onDeletePhoto,
            onNext = onNext
        )

    } else {
        Text(
            text = stringResource(R.string.text_total_paid),
            style = BodyPopBold,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = totalPaid,
            onValueChange = onPaidChange,
            hint = "Masukan total bayar",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = ThousandsSeparatorTransformationHelper(),
            onNext = onNext,
        )
        ErrorText(message = totalPaidError)
    }

    Spacer(Modifier.height(Dimens.MediumMargin))
}


@Composable
private fun TransferSection(
    totalPaid: String,
    totalPaidError: String?,
    banks: List<BankReceiptEntity>?,
    selectedBank: BankReceiptEntity?,
    photos: List<String>,
    photoError: String?,
    bankReceiptError: String?,
    onPaidChange: (String) -> Unit,
    onBankChange: (BankReceiptEntity) -> Unit,
    onAddPhoto: () -> Unit,
    onDeletePhoto: (String) -> Unit,
    onNext: () -> Unit,
) {

    Text(text = "Pilih Bank", style = BodyPopBold, modifier = Modifier.fillMaxWidth())

    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

    CustomDropdownTextField(
        label = "Masukan nama bank yang digunakan",
        items = banks ?: emptyList(),
        selectedItem = selectedBank,
        onItemSelected = onBankChange,
        itemText = { it.bankName },
    )
//    ErrorText(message = bankReceiptError)

    Spacer(Modifier.height(Dimens.MediumMargin))

    Text(
        text = stringResource(R.string.text_total_paid),
        style = BodyPopBold,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))

    CustomTextField(
        modifier = Modifier.fillMaxWidth(),
        value = totalPaid,
        onValueChange = onPaidChange,
        hint = "Masukan total bayar",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = ThousandsSeparatorTransformationHelper(),
        onNext = onNext,
    )

    ErrorText(message = totalPaidError)
    Spacer(Modifier.height(Dimens.MediumMargin))

    Text(
        text = stringResource(R.string.text_evidence_reason),
        style = BodyPopBold,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
    CustomMultiPhotoCard(
        title = "Masukan bukti foto",
        photos = photos,
        onAddPhoto = onAddPhoto,
        onDeletePhoto = onDeletePhoto,
    )
//    ErrorText(message = photoError)

}

@Composable
private fun NotPaySection(
    reasons: List<ReasonEntity>?,
    selectedReason: ReasonEntity?,
    reasonError: String?,
    photoError: String?,
    photos: List<String>,
    onReasonChange: (ReasonEntity) -> Unit,
    onAddPhoto: () -> Unit,
    onDeletePhoto: (String) -> Unit,
) {
    Text(text = "Alasan Tidak Bayar", style = BodyPopBold, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
    CustomDropdownTextField(
        label = "Masukan alasan tidak bayar",
        items = reasons ?: emptyList(),
        selectedItem = selectedReason,
        onItemSelected = onReasonChange,
        itemText = { it.descReason },
    )
    ErrorText(message = reasonError)
    Spacer(Modifier.height(Dimens.MediumMargin))

    Text(
        text = stringResource(R.string.text_evidence_reason),
        style = BodyPopBold,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
    CustomMultiPhotoCard(
        title = "Masukan bukti foto",
        photos = photos,
        onAddPhoto = onAddPhoto,
        onDeletePhoto = onDeletePhoto,
    )
    ErrorText(message = photoError)
}

@Composable
private fun ReceiptSection(
    formattedDateDisplay: String,
    showDateError: Boolean,
    photoErrorForReceipt: String?,
    photos: List<String>,
    onDateFieldClick: () -> Unit,
    onAddPhoto: () -> Unit,
    onDeletePhoto: (String) -> Unit,
) {
    Text(text = "Tanggal Tanda Terima", style = BodyPopBold, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
    CustomTextField(
        value = formattedDateDisplay,
        onValueChange = {},
        hint = "Pilih tanggal tanda terima",
        readOnly = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Pick date",
                tint = Gray,
            )
        },
        onClick = onDateFieldClick,
    )
    if (showDateError) ErrorText(message = "Tanggal wajib dipilih")
    Spacer(Modifier.height(Dimens.MediumMargin))

    Text(
        text = stringResource(R.string.tetx_photo_tt),
        style = BodyPopBold,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
    CustomMultiPhotoCard(
        title = "Masukan bukti tanda terima",
        photos = photos,
        onAddPhoto = onAddPhoto,
        onDeletePhoto = onDeletePhoto,
    )
    ErrorText(message = photoErrorForReceipt)
}

@Composable
private fun ErrorText(message: String?) {
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

@Composable
private fun InvoiceDatePickerDialog(
    initialDateMillis: Long?,
    onConfirm: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis ?: System.currentTimeMillis()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(datePickerState.selectedDateMillis)
            }) {
                Text("Pilih")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}
