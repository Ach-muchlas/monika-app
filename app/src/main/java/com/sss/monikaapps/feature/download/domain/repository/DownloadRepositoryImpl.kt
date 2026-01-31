package com.sss.monikaapps.feature.download.domain.repository

import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_VISIT
import com.sss.monikaapps.common.constanta.TableNameConstant.VISIT_TABLE
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.data.local.DownloadLocalDataSource
import com.sss.monikaapps.feature.download.data.mapper.VisitMapper.serverToEntity
import com.sss.monikaapps.feature.download.data.remote.DownloadRemoteDataSource
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse

class DownloadRepositoryImpl(
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
//            val dataStatusConfig = local.countPendingDownload()
//
//            if (dataStatusConfig == 0) {
//                return Result.error(null, "Semua data sudah selesai didownload")
//            }

            onProgress(0.05f)

            val data =
                remote.fetchDownloadVisit() ?: return Result.error(null, "Data kosong dari server")

            onProgress(0.3f)

            val entities = serverToEntity(data.data ?: emptyList())

            local.insertCustomerVisitBatch(entities) { insertProgress ->
                val totalProgress = 0.3f + (insertProgress * 0.7f)
                onProgress(totalProgress.coerceIn(0f, 0.95f))
            }

            val totalLocal = local.countVisit()
            val totalServer = data.totalData ?: 0

            if (totalLocal != totalServer) {
                local.deleteVisit()
                local.returnDataConfigDownload()
                onProgress(0f)
                return Result.error(
                    null, "Jumlah data tidak cocok. Local=$totalLocal Server=$totalServer"
                )
            }

            local.saveConfigDownload(
                ConfigDownloadDataEntity(
                    id = FEATURE_VISIT,
                    tableName = VISIT_TABLE,
                    totalDataMobile = totalLocal,
                    totalDataServer = totalServer,
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
            val exists = local.checkIsExist(config.tableName)
            if (!exists) {
                local.insertConfigDownload(config)
            }
        }
    }
}