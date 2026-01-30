package com.sss.monikaapps.feature.activity.domain.usecase

import android.util.Log
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDateTime
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.data.mapper.ActivityRequestMapper
import com.sss.monikaapps.feature.activity.data.validator.ActivityValidator
import com.sss.monikaapps.feature.activity.domain.repository.ActivitiesRepository
import com.sss.monikaapps.feature.activity.utils.NetworkChecker

class CreateActivityUseCase(
    private val repository: ActivitiesRepository,
    private val validator: ActivityValidator,
    private val networkChecker: NetworkChecker,
) {

    suspend fun checkIn(payload: ActivityEntity): Result<String> {

        val error = validator.validateCheckInForSubmit(payload)
        repository.insertLogActivities(
            "Check In",
            "Error sebelum insert ke local ${error}. ID - ${payload.id}"
        )
        if (error != null) return Result.error(null, error)

        repository.insertActivity(payload)

        repository.insertLogActivities(
            "Check In",
            "Menyimpan aktivitas ke database lokal. ID = ${payload.id}"
        )

        if (!networkChecker.isConnected()) {
            repository.insertLogActivities(
                "Check In",
                "Tidak ada koneksi internet. Data hanya disimpan secara lokal. ID = ${payload.id}"
            )
            return Result.success("Aktivitas checkin disimpan di perangkat. Silakan sync manual.")
        }

        val entity = repository.fetchDetailActivity(payload.id)
        val photos = repository.fetchPhotosByFeatureId(payload.id, CHECK_IN)

        val request = ActivityRequestMapper.toCheckInRequest(entity, photos)

        return try {
            val serverId = repository.checkInRemote(request)
            repository.insertLogActivities(
                "Check In", "Mencoba mengirim data ke server. ID = ${payload.id}"
            )
            if (!serverId.isNullOrBlank()) {
                repository.markCheckInSynced(payload.id, serverId)
                repository.insertLogActivities(
                    "Check In",
                    "Berhasil mengirimkan data ke server. LocalID = ${payload.id}, ServerID = $serverId"
                )
                Result.success("Aktivitas checkin berhasil dikirim ke server.")
            } else {
                repository.insertLogActivities(
                    "Check In",
                    "Berhasil mengirimkan data ke server tetapi tidak mengembalikan trno. LocalID = ${payload.id}"
                )
                Result.success("Checkin tersimpan lokal, server tidak mengembalikan ID.")
            }

        } catch (e: Exception) {
            repository.insertLogActivities(
                "Check In",
                "Gagal mengirim ke server. Data tetap disimpan lokal. ID = ${payload.id}. Error = ${e.message}"
            )
            Result.success("Checkin tersimpan lokal, gagal sync ke server.")
        }
    }

    suspend fun checkOut(
        idMobile: String,
        latitude: String,
        longitude: String,
        timeEnd: String,
    ): Result<String> {

        // ======================
        // VALIDASI
        // ======================
        val error = validator.validateCheckOut(idMobile)
        if (error != null) return Result.error(null, error)

        // ======================
        // SIMPAN CHECKOUT KE LOKAL DULU
        // ======================
        repository.updateCheckOut(idMobile, timeEnd, latitude, longitude)

        // ======================
        // KALAU OFFLINE → STOP
        // ======================
        if (!networkChecker.isConnected()) {
            return Result.success("Checkout disimpan lokal. Silakan sync manual.")
        }

        // ======================
        // LOAD DATA TERBARU
        // ======================
        var entity = repository.fetchDetailActivity(idMobile)

        // ======================
        // STEP 1: PASTIKAN CHECKIN SUDAH ADA DI SERVER
        // ======================
        if (entity.trno.isNullOrBlank()) {

            val photosIn = repository.fetchPhotosByFeatureId(idMobile, CHECK_IN)

            val errorCheckIn = validator.validateCheckInForSync(entity)
            if (errorCheckIn != null) {
                return Result.error(null, errorCheckIn)
            }

            val requestIn = ActivityRequestMapper.toCheckInRequest(entity, photosIn)
            val serverTrno = repository.checkInRemote(requestIn)

            if (serverTrno.isNullOrBlank()) {
                // ❌ GAGAL CHECKIN → JANGAN LANJUT CHECKOUT
                return Result.success("Checkout disimpan lokal, gagal sync checkin ke server.")
            }

            repository.markCheckInSynced(idMobile, serverTrno)
            repository.markCheckOutIsDone(idMobile)
            entity = repository.fetchDetailActivity(idMobile)
        }

        // ======================
        // STEP 2: PUSH CHECKOUT KE SERVER
        // ======================
        val photosOut = repository.fetchPhotosByFeatureId(idMobile, CHECK_OUT)

        val requestOut = ActivityRequestMapper.toCheckOut(
            idMobile = idMobile,
            timeEnd = timeEnd,
            latitude = latitude,
            longitude = longitude,
            photos = photosOut
        )

        return try {
            val serverId = repository.checkOutRemote(entity.trno!!, requestOut)

            if (!serverId.isNullOrBlank()) {
                repository.markCheckOutSynced(idMobile)
                Result.success("Checkout berhasil dikirim ke server.")
            } else {
                Result.success("Checkout tersimpan lokal, server tidak mengembalikan ID.")
            }

        } catch (e: Exception) {
            Result.success("Checkout tersimpan lokal, gagal sync ke server.")
        }
    }


    suspend fun syncManualDataActivity(): Result<String> {

        if (!networkChecker.isConnected()) {
            return Result.error(null, "Tidak ada koneksi internet")
        }

        val pendingList = repository.fetchActivitiesLocal()

        if (pendingList.isEmpty()) {
            return Result.success("Tidak ada data yang perlu disinkronkan")
        }

        var successCount = 0
        var failedCount = 0

        for (activity in pendingList) {
            try {
                val entity = repository.fetchDetailActivity(activity.id)

                if (entity.trno.isNullOrBlank()) {
                    val photosIn = repository.fetchPhotosByFeatureId(entity.id, CHECK_IN)

                    val error = validator.validateCheckInForSync(entity)
                    if (error != null) {
                        failedCount++
                        continue
                    }

                    val requestIn = ActivityRequestMapper.toCheckInRequest(entity, photosIn)
                    val serverId = repository.checkInRemote(requestIn)

                    if (serverId.isNullOrBlank()) {
                        failedCount++
                        continue
                    }

                    val isDoneCheckOut = entity.activityStatus == 2

                    repository.markCheckInSynced(entity.id, serverId)

                    if (isDoneCheckOut) {
                        repository.markCheckOutIsDone(entity.id)
                    }
                }

                val latest = repository.fetchDetailActivity(activity.id)

                if (latest.activityStatus == 2 && latest.syncStatus == 3) {

                    val photosOut = repository.fetchPhotosByFeatureId(latest.id, CHECK_OUT)

                    val requestOut = ActivityRequestMapper.toCheckOut(
                        idMobile = latest.id,
                        timeEnd = latest.endAt ?: getCurrentDateTime(),
                        latitude = latest.endLatitude ?: "",
                        longitude = latest.endLongitude ?: "",
                        photos = photosOut
                    )

                    val result = repository.checkOutRemote(latest.trno ?: "", requestOut)

                    if (result.isNullOrBlank()) {
                        failedCount++
                        continue
                    }

                    // set syncStatus = 4
                    repository.markCheckOutSynced(latest.id)
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
