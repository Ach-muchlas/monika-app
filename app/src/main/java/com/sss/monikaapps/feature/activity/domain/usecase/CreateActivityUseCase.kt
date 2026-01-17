package com.sss.monikaapps.feature.activity.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.data.local.ActivityLocalDataSource
import com.sss.monikaapps.feature.activity.data.mapper.ActivityRequestMapper
import com.sss.monikaapps.feature.activity.data.remote.ActivityRemoteDataSource
import com.sss.monikaapps.feature.activity.data.validator.ActivityValidator
import com.sss.monikaapps.feature.activity.utils.NetworkChecker


class CreateActivityUseCase(
    private val validator: ActivityValidator,
    private val local: ActivityLocalDataSource,
    private val remote: ActivityRemoteDataSource,
    private val networkChecker: NetworkChecker,
) {

    suspend fun execute(payload: ActivityEntity): Result<String> {

        val error = validator.validateCheckIn(payload)
        if (error != null) return Result.error(null, error)

        local.insertActivity(payload)

        if (!networkChecker.isConnected()) {
            return Result.success("Aktivitas disimpan di perangkat. Silakan sync.")
        }

        val entity = local.getDetail(payload.id)
        val photos = local.getPhotos(payload.id)

        val request = ActivityRequestMapper.toCheckInRequest(entity, photos)

        return try {
            val serverId = remote.checkIn(request)

            if (!serverId.isNullOrBlank()) {
                local.markCheckInSynced(payload.id, serverId)
                Result.success("Aktivitas berhasil disimpan dan dikirim ke server.")
            } else {
                Result.success("Tersimpan lokal, tapi server tidak mengembalikan ID.")
            }

        } catch (e: Exception) {
            Result.success("Tersimpan lokal, tapi gagal sync ke server.")
        }
    }
}
