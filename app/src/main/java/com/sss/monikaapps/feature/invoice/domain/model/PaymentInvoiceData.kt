package com.sss.monikaapps.feature.invoice.domain.model

data class PaymentInvoiceData(
    val customerId: String,
    val customerName: String,
    val nomorNota: String,
    val outstandingNota: Int,
    val nominalNota : Int
)