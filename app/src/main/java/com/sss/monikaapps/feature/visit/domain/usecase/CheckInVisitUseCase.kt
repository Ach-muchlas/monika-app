package com.sss.monikaapps.feature.visit.domain.usecase

import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.network.domain.NetworkChecker
import com.sss.monikaapps.feature.download.data.mapper.VisitMapper
import com.sss.monikaapps.feature.visit.domain.repository.VisitRepository

class CheckInVisitUseCase(
    private val repository: VisitRepository,
    private val networkChecker: NetworkChecker,
) {
    suspend operator fun invoke(
        idVisit: String,
        desc: String,
        timeCheckIn: String,
        startLat: String,
        startLng: String,
    ): Result<String> {

        if (repository.countStillCheckIn() != 0) {
            return Result.error("Gagal", "Gagal checkin, karena data sebelum nya belum di checkout")
        }

        var entity = repository.fetchVisitDetail(idVisit)
        val userLat = startLat.toDoubleOrNull()
        val userLng = startLng.toDoubleOrNull()

        if (userLat == null || userLng == null) {
            return Result.error("Gagal", "Lokasi tidak valid")
        }

        val distanceInMeters = repository.calculateDistanceInMeters(
            customerLat = entity.customerLatitude,
            customerLong = entity.customerLongitude,
            userLat = userLat,
            userLong = userLng
        )

        if (distanceInMeters > 50.0) {
            return Result.error(
                "Gagal",
                "Gagal checkin, karena jarak anda terlalu jauh dari titik pelanggan"
            )
        }

        repository.checkInVisit(idVisit, desc, timeCheckIn, startLat, startLng)

        if (!networkChecker.isConnected()) {
            repository.insertLogActivities(
                "Check In",
                "Tidak ada koneksi internet. Data hanya disimpan secara lokal. ID = ${idVisit}"
            )
            return Result.success("Aktivitas checkin disimpan di perangkat. Silakan sync manual.")
        }

        entity = repository.fetchVisitDetail(idVisit)
        val photos = repository.fetchPhotosByFeatureId(idVisit, CHECK_IN)

        val request = VisitMapper.toCheckInVisitRequest(entity, photos)

        return try {
            val serverId = repository.checkInVisitRemote(request)
            repository.insertLogActivities(
                "Check In", "Mencoba mengirim data ke server. ID = $idVisit"
            )

            if (!serverId.isNullOrBlank()) {
                repository.markCheckInIsSyncVisit(idVisit, serverId)
                repository.insertLogActivities(
                    "Check In",
                    "Berhasil mengirimkan data ke server. LocalID = $idVisit, ServerID = $serverId"
                )
                Result.success("Checkin kunjungan ${entity.customerId} berhasil dikirim ke server.")
            } else {
                repository.insertLogActivities(
                    "Check In",
                    "Berhasil mengirimkan data ke server tetapi tidak mengembalikan trno. LocalID = $idVisit"
                )
                Result.success("Checkin tersimpan lokal, server tidak mengembalikan ID.")
            }

        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred")
        }
    }
}