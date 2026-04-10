package com.sss.monikaapps.feature.download.data.local

import android.content.Context
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import kotlinx.coroutines.flow.Flow

interface DownloadLocalDataSource {
    suspend fun saveConfigDownload(data: ConfigDownloadDataEntity)
    suspend fun insertConfigDownload(data: List<ConfigDownloadDataEntity>)
    suspend fun checkIsExist(tableName: String): Boolean
    suspend fun fetchDataConfig(): List<ConfigDownloadDataEntity>
    suspend fun returnDataVisitCustomerConfigDownload(tableName: String)
    suspend fun countPendingDownload(): Int
    suspend fun getDateConfig(): String

    suspend fun deleteVisit()
    fun countVisit(): Flow<Int>
    suspend fun insertCustomerVisitBatch(
        data: List<VisitEntity>,
        onProgress: (Float) -> Unit,
    )

    suspend fun deleteAllLocalData(
        context: Context,
    )

    suspend fun countDataBankReceipt(): Int
    suspend fun deleteDataBankReceipt()

    fun fetchDataConfig2(): Flow<List<ConfigDownloadDataEntity>>
}
