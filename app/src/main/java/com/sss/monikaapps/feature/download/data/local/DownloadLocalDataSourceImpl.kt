package com.sss.monikaapps.feature.download.data.local

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import com.sss.monikaapps.common.helper.StorageHelper.deleteDir
import com.sss.monikaapps.database.AppDatabase
import com.sss.monikaapps.feature.download.data.dao.ConfigDownloadDataDao
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.visit.data.dao.VisitDao
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File

class DownloadLocalDataSourceImpl(
    private val database: AppDatabase,
    private val dao: VisitDao,
    private val configDao: ConfigDownloadDataDao,
) : DownloadLocalDataSource {

    override suspend fun saveConfigDownload(data: ConfigDownloadDataEntity) {
        configDao.upsertConfigDownload(data)
    }

    override suspend fun insertConfigDownload(data: List<ConfigDownloadDataEntity>) {
        configDao.insertListConfigDownload(data)
    }

    override suspend fun checkIsExist(tableName: String): Boolean {
        return configDao.isTableExist(tableName)
    }

    override suspend fun fetchDataConfig(): List<ConfigDownloadDataEntity> {
        return configDao.fetchDataConfig()
    }

    override suspend fun returnDataVisitCustomerConfigDownload(tableName: String) {
        configDao.returnDataConfigDownload(tableName)
    }

    override suspend fun countPendingDownload(): Int {
        return configDao.countPendingDownload()
    }

    override suspend fun getDateConfig(): String {
        return configDao.getDownloadDate()
    }

    override suspend fun deleteVisit() {
        dao.deleteVisit()
    }

    override fun countVisit(): Flow<Int> = dao.countDataVisit()

    override suspend fun insertCustomerVisitBatch(
        data: List<VisitEntity>,
        onProgress: (Float) -> Unit,
    ) {
        val total = data.size
        var inserted = 0

        data.chunked(500).forEach { batch ->
            dao.insertCustomerVisit(batch)

            inserted += batch.size
            val progress = inserted.toFloat() / total.toFloat()
            onProgress(progress)
        }
    }

    override suspend fun deleteAllLocalData(
        context: Context,
    ) {
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
