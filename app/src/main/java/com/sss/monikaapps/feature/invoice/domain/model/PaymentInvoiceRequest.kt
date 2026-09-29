package com.sss.monikaapps.feature.invoice.domain.model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class PaymentInvoiceRequest(
    val idMobile: String,
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
    val dueDate: String,
    val dateNota: String,
    val amount: String,
    val dateReceipt: String,
    val paymentMethod : String,
    val idCoa : String,
    val photos: List<File>?,
    val createdAtPhotos: List<String>?,
)

fun PaymentInvoiceRequest.toMultipartBody(): Map<String, RequestBody> {
    val mediaType = "text/plain".toMediaTypeOrNull()
    return mutableMapOf<String, RequestBody>().apply {
        put("id_mobile", idMobile.toRequestBody(mediaType))
        put("nomor_nota", nomorNota.toRequestBody(mediaType))
        put("customer_id", customerId.toRequestBody(mediaType))
        put("customer_name", customerName.toRequestBody(mediaType))
        put("customer_address", customerAddress.toRequestBody(mediaType))
        put("customer_phone", customerPhone.toRequestBody(mediaType))
        put("customer_lat", customerLat.toRequestBody(mediaType))
        put("customer_lng", customerLng.toRequestBody(mediaType))
        put("user_lat", userLat.toRequestBody(mediaType))
        put("user_lng", userLng.toRequestBody(mediaType))
        put("is_status", isStatus.toString().toRequestBody(mediaType))
        put("entry_time", entryTime.toRequestBody(mediaType))
        put("outstanding_nota", outstandingNota.toString().toRequestBody(mediaType))
        put("payment", payment.toString().toRequestBody(mediaType))
        put("date_mobile", dateDownload.toRequestBody(mediaType))
        put("id_reason", idReason.toString().toRequestBody(mediaType))
        put("desc_reason", descReason.toRequestBody(mediaType))
        put("selisih_jarak", distanceDifference.toRequestBody(mediaType))
        put("jatuh_tempo", dueDate.toRequestBody(mediaType))
        put("tanggal_nota", dateNota.toRequestBody(mediaType))
        put("nominal_nota", amount.toRequestBody(mediaType))
        put("tanggal_tanda_terima", dateReceipt.toRequestBody(mediaType))
        put("metode_pembayaran", paymentMethod.toRequestBody(mediaType))
        put("id_coa", idCoa.toRequestBody(mediaType))
    }
}

fun PaymentInvoiceRequest.toMultipartImageParts(): List<MultipartBody.Part>? {
    return photos?.map { file ->
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData(
            name = "bukti_foto[]",
            filename = file.name,
            body = requestBody
        )
    }
}

fun PaymentInvoiceRequest.toCreatedAtParts(): List<RequestBody>? {
    val mediaType = "text/plain".toMediaTypeOrNull()
    return createdAtPhotos?.map { it.toRequestBody(mediaType) }
}

