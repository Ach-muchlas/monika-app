package com.sss.monikaapps.feature.activity.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDate
import com.sss.monikaapps.common.mapper.MapperActivity
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.activity.data.local.ActivityLocalDataSource
import com.sss.monikaapps.feature.activity.data.remote.ActivityRemoteDataSource
import com.sss.monikaapps.feature.activity.data.response.DataItemActivities
import com.sss.monikaapps.feature.activity.domain.repository.ActivitiesRepository
import com.sss.monikaapps.feature.activity.utils.NetworkChecker
import kotlinx.coroutines.Dispatchers

class FetchActivitiesUseCase(
    private val repository: ActivitiesRepository,
) {
    suspend fun execute(): Result<List<DataItemActivities>> {
        return try {
            val data = repository.fetchActivities()
            Result.success(data)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Gagal mengambil aktivitas")
        }
    }
}


