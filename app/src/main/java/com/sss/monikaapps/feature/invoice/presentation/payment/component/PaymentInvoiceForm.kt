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
import com.sss.monikaapps.common.formatter.FormatterDate.formatDateToIndoDisplay
import com.sss.monikaapps.common.formatter.FormatterDate.formatTimestampToDateString
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_BG_CHECK
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_PAID
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_RECEIPT
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_UNPAID
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

    var (
        customerData, nomorNota, totalOutstanding, totalNominalNota, totalPaid, statusPaid,
        paymentMethodPaid, paymentMethodReceipt, reasons, selectedReason, listBankReceipt,
        selectedBankReceipt, selectedBankTransferPaid, selectedBankTransferBG, photos,
        dateTransfer, dateReceipt, dateCheck, statusPaymentError, paymentMethodError,
        paymentMethodReceiptError, dateTransferError, totalPaidError, reasonError,
        photoError, bankError, bankReceiptError, dateReceiptError, selectedBankBgError,
        dateBgError, totalPaidBgError,
    ) = state

    val displayDateTransfer = remember(dateTransfer) {
        if (dateTransfer?.isNotEmpty() == true) formatDateToIndoDisplay(dateTransfer) else ""
    }
    val displayDateReceipt = remember(dateReceipt) {
        if (dateReceipt?.isNotEmpty() == true) formatDateToIndoDisplay(dateReceipt) else ""
    }
    val displayDateCheck = remember(dateCheck) {
        if (dateCheck?.isNotEmpty() == true) formatDateToIndoDisplay(dateCheck) else ""
    }

    LaunchedEffect(selectedDateMillis) {
        selectedDateMillis?.let {
            onEvent(PaymentInvoiceEvent.OnDateChange(formatTimestampToDateString(it)))
            selectedDateMillis = null
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
            statusPaid = statusPaid, statusPaymentError, onStatusPaidChange = { value ->
                onEvent(PaymentInvoiceEvent.OnStatusPaidChange(value))
            })

        Spacer(Modifier.height(Dimens.MediumMargin))

        when (statusPaid) {
            STATUS_PAID -> PaySection(
                totalPaid = totalPaid,
                totalPaidError = totalPaidError,
                methodPayment = paymentMethodPaid,
                banks = listBankReceipt,
                photosEvidence = photos,
                formattedDateDisplay = displayDateTransfer,
                selectedBank = selectedBankTransferPaid,
                methodPaymentError = paymentMethodError,
                dateError = dateTransferError,
                photoError = photoError,
                bankReceiptError = bankError,
                onBankChange = { bank -> onEvent(PaymentInvoiceEvent.OnBankSelected(bank)) },
                onAddPhoto = { onEvent(PaymentInvoiceEvent.OnAddPhoto) },
                onDeletePhoto = { photo -> onEvent(PaymentInvoiceEvent.OnDeletePhoto(photo)) },
                onMethodPayment = { method ->
                    onEvent(PaymentInvoiceEvent.OnPaymentMethodChange(method))
                },
                onPaidChange = {
                    onEvent(PaymentInvoiceEvent.OnPaidChange(it))
                },
                onNext = { focusManager.moveFocus(FocusDirection.Down) },

                onDateFieldClick = {
                    showDatePicker = true
                })

            STATUS_UNPAID -> NotPaySection(
                reasons = reasons,
                selectedReason = selectedReason,
                reasonError = reasonError,
                photoError = photoError,
                photos = photos,
                onReasonChange = {
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
                methodPayment = paymentMethodReceipt,
                banks = listBankReceipt,
                formattedDateDisplay = displayDateReceipt,
                selectedBank = selectedBankReceipt,
                methodPaymentError = paymentMethodReceiptError,
                dateError = dateReceiptError,
                photoErrorForReceipt = photoError,
                bankReceiptError = bankReceiptError,
                photos = photos,
                onMethodPayment = { method ->
                    onEvent(PaymentInvoiceEvent.OnPaymentMethodChange(method))
                },
                onBankChange = { bank -> onEvent(PaymentInvoiceEvent.OnBankSelected(bank)) },
                onDateFieldClick = {
                    showDatePicker = true
                },
                onAddPhoto = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                    photoError = null
                    onEvent(PaymentInvoiceEvent.OnAddPhoto)
                },
                onDeletePhoto = { photo -> onEvent(PaymentInvoiceEvent.OnDeletePhoto(photo)) },
            )

            STATUS_BG_CHECK -> BGCheckSection(
                banks = listBankReceipt,
                formattedDateDisplay = displayDateCheck,
                totalPaid = totalPaid,
                photos = photos,
                selectedBank = selectedBankTransferBG,
                selectedBankBgError = selectedBankBgError,
                dateBgError = dateBgError,
                totalPaidBgError = totalPaidBgError,
                photoBgError = photoError,
                onBankChange = { bank -> onEvent(PaymentInvoiceEvent.OnBankSelected(bank)) },
                onDateFieldClick = {
                    showDatePicker = true
                },
                onPaidChange = { onEvent(PaymentInvoiceEvent.OnPaidChange(it)) },
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                onAddPhoto = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                    photoError = null
                    onEvent(PaymentInvoiceEvent.OnAddPhoto)
                },
                onDeletePhoto = { photo -> onEvent(PaymentInvoiceEvent.OnDeletePhoto(photo)) })
        }

        Spacer(Modifier.height(18.dp))

        CustomPrimaryButton(
            text = stringResource(R.string.text_save),
            onClick = {
                onEvent(PaymentInvoiceEvent.OnSubmit(photos))
            },
        )

        Spacer(Modifier.height(Dimens.LargeMargin))
    }

    if (showDatePicker) {
        CustomDatePickerDialog(
            initialDateMillis = selectedDateMillis,
            onConfirm = { millis ->
                selectedDateMillis = millis
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
        )
    }
}

