package com.sss.monikaapps.feature.invoice.presentation.payment.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomDatePickerDialog
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.formatter.FormatterDate
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_BG_CHECK
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_PAID
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_RECEIPT
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_UNPAID
import com.sss.monikaapps.feature.invoice.data.validate.ValidationForm.validateFormPayment
import com.sss.monikaapps.feature.invoice.presentation.payment.PaymentInvoiceEvent
import com.sss.monikaapps.feature.invoice.presentation.payment.PaymentInvoiceUiState

@Composable
fun PaymentInvoiceForm(
    state: PaymentInvoiceUiState,
    onEvent: (PaymentInvoiceEvent) -> Unit,
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
            onEvent(PaymentInvoiceEvent.OnDateChange(FormatterDate.formatTimestampToDateString(it)))
        }
    }

    val (
        customerData,
        nomorNota,
        totalOutstanding,
        totalNominalNota,
        totalPaid,
        statusPaid,
        paymentMethod,
        reasons,
        selectedReason,
        listBankReceipt,
        selectedBankReceipt,
        photos,
    ) = state

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
            statusPaid = statusPaid, onStatusPaidChange = { value ->
                onEvent(PaymentInvoiceEvent.OnStatusPaidChange(value))
            })

        Spacer(Modifier.height(Dimens.MediumMargin))

        when (statusPaid) {
            STATUS_PAID -> PaySection(
                totalPaid = totalPaid,
                totalPaidError = totalPaidError,
                methodPayment = paymentMethod,
                banks = listBankReceipt,
                photosEvidence = photos,
                selectedBank = selectedBankReceipt,
                onBankChange = { bank -> onEvent(PaymentInvoiceEvent.OnBankSelected(bank)) },
                onAddPhoto = { onEvent(PaymentInvoiceEvent.OnAddPhotoTransfer) },
                onDeletePhoto = { photo -> onEvent(PaymentInvoiceEvent.OnDeletePhoto(photo)) },
                onMethodPayment = { method ->
                    onEvent(PaymentInvoiceEvent.OnPaymentMethodChange(method))
                },
                onPaidChange = {
                    totalPaidError = null
                    onEvent(PaymentInvoiceEvent.OnPaidChange(it))
                },
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            )

            STATUS_UNPAID -> NotPaySection(
                reasons = reasons,
                selectedReason = selectedReason,
                reasonError = reasonError,
                photoError = photoError,
                photos = photos,
                onReasonChange = {
                    reasonError = null
                    onEvent(PaymentInvoiceEvent.OnReasonChange(it))
                },
                onAddPhoto = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                    photoError = null
                    onEvent(PaymentInvoiceEvent.OnAddPhoto)
                },
                onDeletePhoto = { photo -> onEvent(PaymentInvoiceEvent.OnDeletePhoto(photo)) },
            )

            STATUS_RECEIPT -> ReceiptSection(
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
                    onEvent(PaymentInvoiceEvent.OnAddPhotoReceipt)
                },
                onDeletePhoto = { photo -> onEvent(PaymentInvoiceEvent.OnDeletePhoto(photo)) },
            )

            STATUS_BG_CHECK -> BGCheckSection(
                banks = listBankReceipt,
                formattedDateDisplay = formattedDateDisplay,
                totalPaid = totalPaid,
                photos = photos,
                selectedBank = selectedBankReceipt,
                onBankChange = {bank -> onEvent(PaymentInvoiceEvent.OnBankSelected(bank)) },
                onDateFieldClick = {},
                onPaidChange = {},
                onNext = {},
                onAddPhoto = {},
                onDeletePhoto = {})
        }

        Spacer(Modifier.height(18.dp))

        CustomPrimaryButton(
            text = stringResource(R.string.text_save),
            onClick = {
                val hasError = validateFormPayment(
                    statusPaid = statusPaid,
                    totalPaid = totalPaid,
                    totalOutstanding = totalOutstanding,
                    selectedReason = selectedReason,
                    photos = photos,
                    onTotalPaidError = { totalPaidError = it },
                    onReasonError = { reasonError = it },
                    onPhotoError = { photoError = it },
                )
                if (!hasError) onEvent(PaymentInvoiceEvent.OnSubmit)
            },
        )

        Spacer(Modifier.height(Dimens.LargeMargin))
    }

    if (showDatePicker) {
        CustomDatePickerDialog(
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

