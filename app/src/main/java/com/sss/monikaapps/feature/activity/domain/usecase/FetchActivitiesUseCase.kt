package com.sss.monikaapps.feature.activity.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.activity.data.response.DataItemActivities
import com.sss.monikaapps.feature.activity.domain.repository.ActivitiesRepository

class FetchActivitiesUseCase(
    private val repository: ActivitiesRepository,
) {
    suspend operator fun invoke(): Result<List<DataItemActivities>> {
        return try {
            val data = repository.fetchActivities()
            Result.success(data)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Gagal mengambil aktivitas")
        }
    }
}


