package com.sss.monikaapps.feature.download.data.local

import android.content.Context
import androidx.room.Database
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity

interface DownloadLocalDataSource {
    suspend fun saveConfigDownload(data: ConfigDownloadDataEntity)
    suspend fun saveConfigDownloadList(data : List<ConfigDownloadDataEntity>)
    suspend fun insertConfigDownload(data: ConfigDownloadDataEntity)
    suspend fun checkIsExist(tableName: String): Boolean
    suspend fun fetchDataConfig(): List<ConfigDownloadDataEntity>
    suspend fun returnDataVisitCustomerConfigDownload(tableName: String)
    suspend fun countPendingDownload(): Int
    suspend fun getDateConfig(): String

    suspend fun deleteVisit()
    suspend fun countVisit(): Int
    suspend fun insertCustomerVisitBatch(
        data: List<VisitEntity>,
        onProgress: (Float) -> Unit,
    )

    suspend fun deleteAllLocalData(
        context: Context,
    )
}
