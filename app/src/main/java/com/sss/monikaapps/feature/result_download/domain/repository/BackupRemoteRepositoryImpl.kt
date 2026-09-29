package com.sss.monikaapps.feature.result_download.domain.repository

import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.result_download.data.remote.BackupRemoteDataSource
import java.io.File

class BackupRemoteRepositoryImpl(private val remote: BackupRemoteDataSource) :
    BackupRemoteRepository {
    override suspend fun sendEmail(reason: String, file: File): DefaultAddResponse? {
        return remote.sendEmail(reason, file)
    }
}