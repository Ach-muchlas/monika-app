package com.sss.monikaapps.feature.download.data.local

import com.sss.monikaapps.feature.download.data.dao.ConfigDownloadDataDao
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.visit.data.dao.VisitDao
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity

class DownloadLocalDataSourceImpl(
    private val dao: VisitDao,
    private val configDao: ConfigDownloadDataDao,
) : DownloadLocalDataSource {

    override suspend fun saveConfigDownload(data: ConfigDownloadDataEntity) {
        configDao.upsertConfigDownload(data)
    }

    override suspend fun insertConfigDownload(data: ConfigDownloadDataEntity) {
        configDao.insertConfigDownload(data)
    }

    override suspend fun checkIsExist(tableName: String): Boolean {
        return configDao.isTableExist(tableName)
    }

    override suspend fun fetchDataConfig(): List<ConfigDownloadDataEntity> {
        return configDao.fetchDataConfig()
    }

    override suspend fun returnDataConfigDownload() {
        configDao.returnDataConfigDownload()
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

    override suspend fun countVisit(): Int = dao.countDataVisit()

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
}