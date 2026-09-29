package com.sss.monikaapps.feature.visit.domain.usecase

import android.util.Log
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.activity.data.response.PhotoItem
import com.sss.monikaapps.feature.visit.domain.model.DataItemDetailVisitLocal
import com.sss.monikaapps.feature.visit.domain.repository.VisitRepository

class FetchVisitDetailUseCase(private val repository: VisitRepository) {
    suspend operator fun invoke(idVisit: String): Result<DataItemDetailVisitLocal> {
        return try {
            val dataHeader = repository.fetchVisitDetail(idVisit)
            val status = when (dataHeader.syncStatus) {
                0, 1, 2 -> "1"
                3, 4 -> "2"
                else -> "0"
            }
            Log.e("CHECK_DATA_USE_CASE", "data status : $status")
            Log.e("CHECK_DATA_USE_CASE", "data status header: ${dataHeader.syncStatus}")

            val dataPhotos = repository.fetchPhotoByParentId(dataHeader.id)
            val photos =PhotoItem()
                Result.success(
                DataItemDetailVisitLocal(
                    header = dataHeader,
                    photos = dataPhotos
                )
            )
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!!")
        }
    }
}