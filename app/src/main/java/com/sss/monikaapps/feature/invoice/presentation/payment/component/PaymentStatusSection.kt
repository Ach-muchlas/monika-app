package com.sss.monikaapps.feature.invoice.presentation.payment.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomCheckbox
import com.sss.monikaapps.common.component.CustomDropdownTextField
import com.sss.monikaapps.common.component.CustomTextField
import com.sss.monikaapps.common.helper.ThousandsSeparatorTransformationHelper
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray
import com.sss.monikaapps.feature.invoice.data.const.PaymentMethodConst.PAYMENT_CASH
import com.sss.monikaapps.feature.invoice.data.const.PaymentMethodConst.PAYMENT_TRANSFER
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_BG_CHECK
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_PAID
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_RECEIPT
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_UNPAID
import com.sss.monikaapps.feature.invoice.data.entity.BankReceiptEntity
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity
import com.sss.monikaapps.feature.photo.CustomMultiPhotoCard

//@Composable
//fun PaymentStatusSelector(
//    statusPaid: Int,
//    onStatusPaidChange: (Int) -> Unit,
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth(),
//    ) {
//        StatusCheckboxItem(
//            label = "Bayar",
//            checked = statusPaid == STATUS_PAID,
//            onClick = { onStatusPaidChange(STATUS_PAID) },
//        )
//
//        Spacer(Modifier.width(Dimens.LargeMargin))
//
//        StatusCheckboxItem(
//            label = "Tidak Bayar",
//            checked = statusPaid == STATUS_UNPAID,
//            onClick = { onStatusPaidChange(STATUS_UNPAID) },
//        )
//    }
//
//    Spacer(Modifier.height(Dimens.MediumMargin))
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth(),
//    ) {
//
//        StatusCheckboxItem(
//            label = "Tanda Terima",
//            checked = statusPaid == STATUS_RECEIPT,
//            onClick = { onStatusPaidChange(STATUS_RECEIPT) },
//        )
//
//        Spacer(Modifier.width(Dimens.LargeMargin))
//
//        StatusCheckboxItem(
//            label = "BG",
//            checked = statusPaid == STATUS_BG_CHECK,
//            onClick = { onStatusPaidChange(STATUS_BG_CHECK) },
//        )
//    }
//}


@Composable
fun PaymentStatusSelector(
    statusPaid: Int,
    onStatusPaidChange: (Int) -> Unit,
) {
    val statuses = listOf(
        STATUS_PAID to "Bayar",
        STATUS_UNPAID to "Tidak Bayar",
        STATUS_RECEIPT to "Tanda Terima",
        STATUS_BG_CHECK to "BG/Cek"
    )

    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SmallMargin)) {
        // Baris 1: Bayar & Tidak Bayar
        Row(Modifier.fillMaxWidth()) {
            StatusItem(statuses[0], statusPaid, onStatusPaidChange, Modifier.weight(1f))
            StatusItem(statuses[1], statusPaid, onStatusPaidChange, Modifier.weight(1f))
        }
        // Baris 2: Tanda Terima & BG/Cek
        Row(Modifier.fillMaxWidth()) {
            StatusItem(statuses[2], statusPaid, onStatusPaidChange, Modifier.weight(1f))
            StatusItem(statuses[3], statusPaid, onStatusPaidChange, Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatusItem(
    item: Pair<Int, String>,
    currentStatus: Int,
    onStatusChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable { onStatusChange(item.first) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomCheckbox(checked = currentStatus == item.first, onCheckedChange = { onStatusChange(item.first) })
        Spacer(Modifier.width(Dimens.SmallMargin))
        Text(text = item.second, style = BodyPopBold)
    }
}


@Composable
fun PaySection(
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
//            photoError = "",
//            bankReceiptError = "",
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
fun TransferSection(
    totalPaid: String,
    totalPaidError: String?,
    banks: List<BankReceiptEntity>?,
    selectedBank: BankReceiptEntity?,
    photos: List<String>,
//    photoError: String?,
//    bankReceiptError: String?,
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
fun NotPaySection(
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
fun ReceiptSection(
    formattedDateDisplay: String,
    showDateError: Boolean,
    photoErrorForReceipt: String?,
    photos: List<String>,
    onDateFieldClick: () -> Unit,
    onAddPhoto: () -> Unit,
    onDeletePhoto: (String) -> Unit,
) {
    Text(text = "Tanggal Kembali", style = BodyPopBold, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
    CustomTextField(
        value = formattedDateDisplay,
        onValueChange = {},
        hint = "Pilih tanggal kembali",
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
fun BGCheckSection(
    banks: List<BankReceiptEntity>?,
    formattedDateDisplay: String,
    totalPaid: String,
    photos: List<String>,
    selectedBank: BankReceiptEntity?,
    //        totalPaidError: String?,
//        showDateError: Boolean,
//        photoErrorForReceipt: String?,
    onBankChange: (BankReceiptEntity) -> Unit,
    onDateFieldClick: () -> Unit,
    onPaidChange: (String) -> Unit,
    onNext: () -> Unit,
    onAddPhoto: () -> Unit,
    onDeletePhoto: (String) -> Unit,

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

    Text(text = "Tanggal Cair", style = BodyPopBold, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
    CustomTextField(
        value = formattedDateDisplay,
        onValueChange = {},
        hint = "Pilih tanggal cair",
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
//        if (showDateError) ErrorText(message = "Tanggal wajib dipilih")
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

//        ErrorText(message = totalPaidError)

    Spacer(Modifier.height(Dimens.MediumMargin))
    Text(
        text = stringResource(R.string.tetx_photo_tt),
        style = BodyPopBold,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
    CustomMultiPhotoCard(
        title = "Masukan bukti BG",
        photos = photos,
        onAddPhoto = onAddPhoto,
        onDeletePhoto = onDeletePhoto,
    )
//        ErrorText(message = photoErrorForReceipt)
}


@Composable
fun PaymentMethodSelector(
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
fun StatusCheckboxItem(
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