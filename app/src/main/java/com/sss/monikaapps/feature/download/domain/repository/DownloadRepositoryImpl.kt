package com.sss.monikaapps.feature.download.domain.repository

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_VISIT
import com.sss.monikaapps.common.constanta.TableNameConstant.VISIT_TABLE
import com.sss.monikaapps.common.formatter.FormatterDate
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.database.AppDatabase
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.data.local.DownloadLocalDataSource
import com.sss.monikaapps.feature.download.data.mapper.VisitMapper.serverToEntity
import com.sss.monikaapps.feature.download.data.remote.DownloadRemoteDataSource
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DownloadRepositoryImpl(
    private val context: Context,
    private val database: AppDatabase,
    private val remote: DownloadRemoteDataSource,
    private val local: DownloadLocalDataSource,
) : DownloadRepository {
    override suspend fun fetchDataConfigDownload(): Result<List<ConfigDownloadDataEntity>> {
        return try {
            val data = local.fetchDataConfig()
            Result.success(data)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!!")
        }
    }

    override suspend fun fetchDownload(
        onProgress: (Float) -> Unit,
    ): Result<VisitDownloadResponse> {
        return try {

            onProgress(0.05f)

            deleteAllLocalData()
            deleteAppStorage()
            clearAppCache()

            local.saveConfigDownload(
                ConfigDownloadDataEntity(
                    id = FEATURE_VISIT,
                    tableName = VISIT_TABLE,
                    totalDataMobile = 0,
                    totalDataServer =  0,
                    statusTotalDownload = false
                )
            )

            val data =
                remote.fetchDownloadVisit() ?: return Result.error(null, "Data kosong dari server")

            onProgress(0.3f)

            val entities = serverToEntity(data.data ?: emptyList())

            local.insertCustomerVisitBatch(entities) { insertProgress ->
                val totalProgress = 0.3f + (insertProgress * 0.7f)
                onProgress(totalProgress.coerceIn(0f, 0.95f))
            }

            val totalLocal = local.countVisit()
            val checkData = remote.checkDataDownloadVisit(totalLocal)

            if (checkData != "1") {
                local.deleteVisit()
                local.returnDataConfigDownload()
                onProgress(0f)
                return Result.error(
                    null, "Jumlah data tidak cocok. Local = $totalLocal Server = ${data.totalData}"
                )
            }

            local.saveConfigDownload(
                ConfigDownloadDataEntity(
                    id = FEATURE_VISIT,
                    tableName = VISIT_TABLE,
                    totalDataMobile = totalLocal,
                    totalDataServer = data.totalData ?: 0,
                    statusTotalDownload = true
                )
            )

            onProgress(1f)

            Result.success(data)

        } catch (e: Exception) {
            onProgress(0f)
            Result.error(null, e.message ?: "Error Occurred")
        }
    }

    override suspend fun countDataPending(): Int {
        return local.countPendingDownload()
    }

    override suspend fun insertDownloadData(data: List<ConfigDownloadDataEntity>) {
        data.map { config ->
            if (local.getDateConfig() != FormatterDate.getCurrentDate() ){
                local.saveConfigDownload(config)
            }else {
                val exists = local.checkIsExist(config.tableName)
                if (!exists) {
                    local.insertConfigDownload(config)
                }
            }
        }
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            if (children != null) {
                for (child in children) {
                    val success = deleteDir(File(dir, child))
                    if (!success) {
                        return false
                    }
                }
            }
        }
        return dir.delete()
    }

    private fun clearAppCache() {
        try {
            val cacheDir = context.cacheDir
            deleteDir(cacheDir)
        } catch (e: Exception) {
            Log.e("SUBMIT", "Failed to clear cache: ${e.message}")
        }
    }

    private fun deleteAppStorage() {
        context.filesDir?.let { deleteDir(it) }
        context.cacheDir?.let { deleteDir(it) }
        context.externalCacheDir?.let { deleteDir(it) }
        context.getExternalFilesDir(null)?.let { deleteDir(it) }
    }

    private suspend fun deleteAllLocalData() {
        withContext(Dispatchers.IO) {
            try {
                // 1. Clear Room Database
                database.clearAllTables()

                // 2. Hapus file foto
                val photoDir = File(context.filesDir, "photo")
                if (photoDir.exists()) {
                    deleteDir(photoDir)
                }

                try {
                    val db = (database as RoomDatabase)
                        .openHelper
                        .writableDatabase
                    db.execSQL("VACUUM")
                } catch (e: Exception) {
                    Log.e("CLEAR_DATA", "VACUUM error: ${e.message}")
                }

            } catch (e: Exception) {
                Log.e("CLEAR_DATA", "Delete local data error: ${e.message}")
            }
        }
    }
}