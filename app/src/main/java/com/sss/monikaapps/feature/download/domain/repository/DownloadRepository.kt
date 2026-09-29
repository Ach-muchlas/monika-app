package com.sss.monikaapps.feature.download.domain.repository

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.data.model.DownloadDataResponse
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    suspend fun fetchDataConfigDownload(): Result<List<ConfigDownloadDataEntity>>
    suspend fun fetchDownload(
        onProgress: (Float, String) -> Unit,
    ): Result<DownloadDataResponse>

    fun fetchDataConfig2(): Flow<List<ConfigDownloadDataEntity>>

    suspend fun countDataPending(): Int
    suspend fun insertDownloadData(data: List<ConfigDownloadDataEntity>)

    suspend fun listTableConfig(): List<ConfigDownloadDataEntity>

    fun getInvoiceCount(): Flow<Int>
    fun getVisitCount(): Flow<Int>
}