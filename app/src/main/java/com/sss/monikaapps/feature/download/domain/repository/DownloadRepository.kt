package com.sss.monikaapps.feature.download.domain.repository

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse

interface DownloadRepository {
    suspend fun fetchDataConfigDownload(): Result<List<ConfigDownloadDataEntity>>
    suspend fun fetchDownload(
        onProgress: (Float) -> Unit,
    ): Result<VisitDownloadResponse>

    suspend fun countDataPending(): Int
    suspend fun insertDownloadData(data: List<ConfigDownloadDataEntity>)
}