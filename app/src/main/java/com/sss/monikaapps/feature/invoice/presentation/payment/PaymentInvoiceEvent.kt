package com.sss.monikaapps.feature.invoice.presentation.payment

import androidx.compose.animation.core.copy
import com.sss.monikaapps.feature.invoice.data.entity.BankReceiptEntity
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity

sealed class PaymentInvoiceEvent {
    data class OnDateChange(val date: String) : PaymentInvoiceEvent()
    data class OnPaidChange(val value: String) : PaymentInvoiceEvent()
    data class OnStatusPaidChange(val status: Int) : PaymentInvoiceEvent()
    data class OnPaymentMethodChange(val method: Int) : PaymentInvoiceEvent()
    data class OnReasonChange(val reason: ReasonEntity) : PaymentInvoiceEvent()
    data class OnBankSelected(val bank: BankReceiptEntity) : PaymentInvoiceEvent()
    object OnAddPhoto : PaymentInvoiceEvent()
    data class OnDeletePhoto(val path: String) : PaymentInvoiceEvent()
    data class OnLocationResult(val lat: String, val lng: String) : PaymentInvoiceEvent()
    data class OnSubmit(val photos: List<String>) : PaymentInvoiceEvent()

    // Di PaymentInvoiceEvent.kt
    object ClearPhotoError : PaymentInvoiceEvent()

}