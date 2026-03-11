package com.sss.monikaapps.feature.invoice.domain.model

data class PaymentInvoiceRequestDataLocal(
    val idNota: String,
    val nomorNota: String,
    val customerId: String,
    val customerName: String,
    val customerAddress: String,
    val customerPhone: String,
    val customerLat: String,
    val customerLng: String,
    val userLat: String,
    val userLng: String,
    val isStatus: Int,
    val entryTime: String,
    val outstandingNota: String,
    val payment: Long,
    val dateDownload: String,
    val idReason: Int,
    val descReason: String,
    val distanceDifference: String,

    )