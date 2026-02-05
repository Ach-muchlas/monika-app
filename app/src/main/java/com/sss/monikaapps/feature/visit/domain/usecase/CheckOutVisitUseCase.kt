package com.sss.monikaapps.feature.visit.domain.usecase

import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.activity.data.mapper.ActivityRequestMapper
import com.sss.monikaapps.feature.activity.utils.NetworkChecker
import com.sss.monikaapps.feature.download.data.mapper.VisitMapper
import com.sss.monikaapps.feature.visit.domain.repository.VisitRepository

class CheckOutVisitUseCase(
    private val repository: VisitRepository,
    private val networkChecker: NetworkChecker,
) {
    suspend operator fun invoke(
        idVisit: String,
        timeCheckIn: String,
        endLat: String,
        endLng: String,
    ): Result<String> {

        repository.checkOutVisit(idVisit, timeCheckIn, endLat, endLng)
        Result.success("Berhasil Checkout ")

        if (!networkChecker.isConnected()) {
            repository.insertLogActivities(
                "Check Out",
                "Tidak ada koneksi internet. Data hanya disimpan secara lokal. ID = $idVisit"
            )
            return Result.success("Aktivitas checkout disimpan di perangkat. Silakan sync manual.")
        }

        var entity = repository.fetchVisitDetail(idVisit)
        if (entity.trno.isNullOrBlank()) {

            val photosIn = repository.fetchPhotosByFeatureId(idVisit, CHECK_IN)

            val requestIn = VisitMapper.toCheckInVisitRequest(entity, photosIn)
            val serverTrno = repository.checkInVisitRemote(requestIn)

            if (serverTrno.isNullOrBlank()) {
                return Result.success("Checkout disimpan lokal, gagal sync checkin ke server.")
            }

            repository.markCheckInIsSyncVisit(idVisit, serverTrno)
            repository.markCheckOutIsDoneVisit(idVisit)
            entity = repository.fetchVisitDetail(idVisit)
        }

        val photoOut = repository.fetchPhotosByFeatureId(idVisit, CHECK_OUT)

        val requestCheckOut = VisitMapper.toCheckOutVisitRequest(entity, photoOut)

        return try {
            val serverId = repository.checkOutVisitRemote(entity.trno!!, requestCheckOut)

            if (!serverId.isNullOrBlank()) {
                repository.markCheckOutIsSyncVisit(idVisit)
                Result.success("Checkout berhasil dikirim ke server.")
            } else {
                Result.success("Checkout tersimpan lokal, server tidak mengembalikan ID.")
            }

        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred")
        }
    }
}