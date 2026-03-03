package com.sss.monikaapps.feature.visit.domain.usecase

import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.network.domain.NetworkChecker
import com.sss.monikaapps.feature.download.data.mapper.VisitMapper
import com.sss.monikaapps.feature.visit.domain.repository.VisitRepository

class SyncManualVisitUseCase(
    private val repository: VisitRepository,
    private val networkChecker: NetworkChecker,
) {
    suspend operator fun invoke(): Result<String> {
        if (!networkChecker.isConnected()) {
            return Result.error(null, "Tidak terkoneksi internet")
        }

        val pendingList = repository.fetchVisitNotSync()

        if (pendingList.isEmpty()) {
            return Result.success("Tidak ada data yang perlu disinkronkan")
        }

        var successCount = 0
        var failedCount = 0

        for (visit in pendingList) {
            try {
                val entity = repository.fetchVisitDetail(visit.id)

                if (entity.trno.isNullOrBlank()) {
                    val photosIn = repository.fetchPhotosByFeatureId(entity.id, CHECK_IN)


                    val requestIn = VisitMapper.toCheckInVisitRequest(entity, photosIn)
                    val serverId = repository.checkInVisitRemote(requestIn)

                    if (serverId.isNullOrBlank()) {
                        failedCount++
                        continue
                    }

                    val isDoneCheckOut = entity.syncStatus == 3

                    repository.markCheckInIsSyncVisit(entity.id, serverId)

                    if (isDoneCheckOut) {
                        repository.markCheckOutIsDoneVisit(entity.id)
                    }
                }

                val latest = repository.fetchVisitDetail(visit.id)

                if (latest.syncStatus == 3) {

                    val photosOut = repository.fetchPhotosByFeatureId(latest.id, CHECK_OUT)

                    val requestOut = VisitMapper.toCheckOutVisitRequest(
                        entity = latest,
                        photos = photosOut
                    )

                    val result = repository.checkOutVisitRemote(latest.trno ?: "", requestOut)

                    if (result.isNullOrBlank()) {
                        failedCount++
                        continue
                    }

                    // set syncStatus = 4
                    repository.markCheckOutIsSyncVisit(latest.id)
                }

                successCount++

            } catch (e: Exception) {
                failedCount++
            }
        }

        return Result.success(
            "Sinkronisasi selesai. Berhasil: $successCount, Gagal: $failedCount"
        )

    }
}