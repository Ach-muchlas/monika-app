package com.sss.monikaapps.feature.result_download.data.remote

import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.network.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class BackupRemoteDataSourceImpl(private val apiService: ApiService) : BackupRemoteDataSource {
    override suspend fun sendEmail(reason: String, file: File): DefaultAddResponse? {
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val reasonBody = reason.toRequestBody("text/plain".toMediaTypeOrNull())

        val response = apiService.sendEmail(
            reason = reasonBody,
            file = filePart
        )

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }
}