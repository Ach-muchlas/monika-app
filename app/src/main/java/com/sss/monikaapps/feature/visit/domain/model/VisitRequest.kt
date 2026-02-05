package com.sss.monikaapps.feature.visit.domain.model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class CheckInVisitRequest(
    val description: String,
    val startAt: String,
    val startLatitude: String,
    val startLongitude: String,
    val trnoMobile: String,
    val customerId: String,
    val customerName: String,
    val customerAddress: String,
    val customerLat: String,
    val customerLng: String,
    val customerKel: String,
    val customerKec: String,
    val customerKab: String,
    val customerProv: String,
    val photos: List<File>,
)

data class CheckOutVisitRequest(
    val endAt: String,
    val endLatitude: String,
    val endLongitude: String,
    val trnoMobile: String,
    val photos: List<File>,
)

fun CheckInVisitRequest.toMultipartBody(): Map<String, RequestBody> {
    return mutableMapOf<String, RequestBody>().apply {
        put("description", description.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("start_at", startAt.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("start_lat", startLatitude.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("start_lng", startLongitude.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("trno_mobile", trnoMobile.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("id_cust", customerId.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("nama_cust", customerName.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("alamat_cust", customerAddress.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("lat_cust", customerLat.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("lng_cust", customerLng.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("kel_cust", customerKel.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("kec_cust", customerKec.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("kab_cust", customerKab.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("prov_cust", customerProv.toRequestBody("text/plain".toMediaTypeOrNull()))
    }
}

fun CheckInVisitRequest.toMultipartImageParts(): List<MultipartBody.Part> {
    return photos.map { file ->
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        createFormData(
            name = "bukti_foto[]",
            filename = file.name,
            body = requestBody
        )
    }
}


fun CheckOutVisitRequest.toMultipartBody(): Map<String, RequestBody> {
    return mutableMapOf<String, RequestBody>().apply {
        put("end_at", endAt.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("end_lat", endLatitude.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("end_lng", endLongitude.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("trno_mobile", trnoMobile.toRequestBody("text/plain".toMediaTypeOrNull()))
    }
}

fun CheckOutVisitRequest.toMultipartImageParts(): List<MultipartBody.Part> {
    return photos.map { file ->
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        createFormData(
            name = "bukti_foto[]",
            filename = file.name,
            body = requestBody
        )
    }
}