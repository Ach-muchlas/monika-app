package com.sss.monikaapps.feature.visit.domain.usecase

import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.mapper.VisitMapper
import com.sss.monikaapps.feature.visit.domain.repository.VisitRepository
import com.sss.monikaapps.network.domain.NetworkChecker

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

        var entity = repository.fetchVisitDetail(idVisit)
        val userLat = endLat.toDoubleOrNull()
        val userLng = endLng.toDoubleOrNull()

        if (userLat == null || userLng == null) {
            return Result.error("Gagal", "Lokasi tidak valid")
        }

        repository.checkOutVisit(idVisit, timeCheckIn, endLat, endLng)

        if (!networkChecker.isConnected()) {
            repository.insertLogActivities(
                "Check Out",
                "Tidak ada koneksi internet. Data hanya disimpan secara lokal. ID = $idVisit"
            )
            return Result.success("Aktivitas checkout disimpan di perangkat. Silakan sync manual.")
        }

        if (entity.trno.isNullOrBlank()) {

            val syncCheckInResult = try {
                val photosIn = repository.fetchPhotosByFeatureId(idVisit, CHECK_IN)

                val requestIn = VisitMapper.toCheckInVisitRequest(entity, photosIn)
                val serverTrno = repository.checkInVisitRemote(requestIn)

                if (!serverTrno.isNullOrBlank()) {
                    repository.markCheckInIsSyncVisit(idVisit, serverTrno)
                    repository.markCheckOutIsDoneVisit(idVisit)
                    serverTrno
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }

            if (syncCheckInResult.isNullOrBlank()) {
                return Result.success(
                    "Checkout disimpan lokal. Check-in belum berhasil disinkron ke server."
                )
            }

            entity = repository.fetchVisitDetail(idVisit)
        }

        val trnoServer = entity.trno
            ?: return Result.success(
                "Checkout disimpan lokal. TRNO belum tersedia untuk sync."
            )

        val photoOut = repository.fetchPhotosByFeatureId(idVisit, CHECK_OUT)

        val requestCheckOut = VisitMapper.toCheckOutVisitRequest(entity, photoOut)

        return try {
            val serverId = repository.checkOutVisitRemote(trnoServer, requestCheckOut)

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