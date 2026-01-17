package com.sss.monikaapps.feature.activity.domain.model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class ActivityCheckInRequest(
    val title: String,
    val description: String,
    val startAt: String,
    val startLatitude: String,
    val startLongitude: String,
    val trnoMobile: String,
    val photoActivity: List<File>,
)

data class ActivityCheckOutRequest(
    val endAt: String,
    val endLatitude: String,
    val endLongitude: String,
    val trnoMobile: String,
    val photoActivity: List<File>,
)


fun ActivityCheckInRequest.toMultipartBody(): Map<String, RequestBody> {
    return mutableMapOf<String, RequestBody>().apply {
        put("title", title.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("description", description.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("start_at", startAt.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("start_lat", startLatitude.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("start_lng", startLongitude.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("trno_mobile", trnoMobile.toRequestBody("text/plain".toMediaTypeOrNull()))
    }
}

fun ActivityCheckInRequest.toMultipartImageParts(): List<MultipartBody.Part> {
    return photoActivity.map { file ->
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        createFormData(
            name = "bukti_foto[]",
            filename = file.name,
            body = requestBody
        )
    }
}


fun ActivityCheckOutRequest.toMultipartBody(): Map<String, RequestBody> {
    return mutableMapOf<String, RequestBody>().apply {
        put("end_at", endAt.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("end_lat", endLatitude.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("end_lng", endLongitude.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("trno_mobile", trnoMobile.toRequestBody("text/plain".toMediaTypeOrNull()))
    }
}

fun ActivityCheckOutRequest.toMultipartImageParts(): List<MultipartBody.Part> {
    return photoActivity.map { file ->
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        createFormData(
            name = "bukti_foto[]",
            filename = file.name,
            body = requestBody
        )
    }
}

