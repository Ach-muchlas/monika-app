package com.sss.monikaapps.feature.result_download.data.remote

import com.sss.monikaapps.common.response.DefaultAddResponse
import java.io.File

interface BackupRemoteDataSource {
    suspend fun sendEmail(reason : String, file : File) : DefaultAddResponse?
}