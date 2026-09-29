package com.sss.monikaapps.feature.expense.domain.model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class ExpenseDetailRequest(
    val idCategoryExpense: String,
    val initialKilometer: String? = "0",
    val finalKilometer: String? = "0",
    val netAmount: String,
    val note: String? = null,
    val photos: List<File>?,
)

data class ExpenseUpdateDetailRequest(
    val initialKilometer: String? = "0",
    val finalKilometer: String? ="0",
    val netAmount: String,
    val note: String? = null,
    val photos: List<File>?,
)

fun ExpenseUpdateDetailRequest.toMultipartBody(): Map<String, RequestBody> {
    return mutableMapOf<String, RequestBody>().apply {
        put("km_awal", initialKilometer.toString().toRequestBody("text/plain".toMediaTypeOrNull()))
        put("km_akhir", finalKilometer.toString().toRequestBody("text/plain".toMediaTypeOrNull()))
        put("netamt", netAmount.toRequestBody("text/plain".toMediaTypeOrNull()))
        note?.toRequestBody("text/plain".toMediaTypeOrNull())?.let { put("note", it) }
    }
}


fun ExpenseDetailRequest.toMultipartBody(): Map<String, RequestBody> {
    return mutableMapOf<String, RequestBody>().apply {
        put("id_pengeluaran", idCategoryExpense.toRequestBody("text/plain".toMediaTypeOrNull()))
        put("km_awal", initialKilometer.toString().toRequestBody("text/plain".toMediaTypeOrNull()))
        put("km_akhir", finalKilometer.toString().toRequestBody("text/plain".toMediaTypeOrNull()))
        put("netamt", netAmount.toRequestBody("text/plain".toMediaTypeOrNull()))
        note?.toRequestBody("text/plain".toMediaTypeOrNull())?.let { put("note", it) }
    }
}

fun ExpenseDetailRequest.toMultipartImageParts(): List<MultipartBody.Part> {
    if (photos.isNullOrEmpty()) {
        val emptyBody =
            "".toRequestBody("text/plain".toMediaTypeOrNull())

        return listOf(
            createFormData(
                name = "foto_nota[]",
                filename = "",
                body = emptyBody
            )
        )
    }

    return photos.map { file ->
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        createFormData(
            name = "foto_nota[]",
            filename = file.name,
            body = requestBody
        )
    }
}

fun ExpenseUpdateDetailRequest.toMultipartImageParts(): List<MultipartBody.Part> {
    if (photos.isNullOrEmpty()) {
        val emptyBody =
            "".toRequestBody("text/plain".toMediaTypeOrNull())

        return listOf(
            createFormData(
                name = "foto_nota[]",
                filename = "",
                body = emptyBody
            )
        )
    }

    return photos.map { file ->
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        createFormData(
            name = "foto_nota[]",
            filename = file.name,
            body = requestBody
        )
    }
}
