package com.sss.monikaapps.feature.result_download.domain.repository

import com.sss.monikaapps.common.response.DefaultAddResponse
import java.io.File

interface BackupRemoteRepository {
    suspend fun sendEmail(reason : String, file : File) : DefaultAddResponse?
}