package com.sss.monikaapps.feature.activity.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sss.monikaapps.common.mapper.MapperActivity
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.activity.data.local.ActivityLocalDataSource
import com.sss.monikaapps.feature.activity.data.remote.ActivityRemoteDataSource
import com.sss.monikaapps.feature.activity.data.response.DataItemDetailActivity
import com.sss.monikaapps.feature.activity.domain.repository.ActivitiesRepository
import com.sss.monikaapps.feature.activity.utils.NetworkChecker
import kotlinx.coroutines.Dispatchers

class FetchDetailActivityUseCase(
    private val repository: ActivitiesRepository,

) {

    suspend fun fetchLocal(
        trno: String,
    ): Result<DataItemDetailActivity> {
        return try {
            val data = repository.fetchDetailLocal(trno)
            Result.success(data)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Gagal mengambil detail aktivitas lokal")
        }
    }

    suspend fun fetchRemote(
        trno: String
    ): Result<DataItemDetailActivity> {
        return try {
            val data = repository.fetchDetailRemote(trno)
            Result.success(data)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Gagal mengambil detail aktivitas dari server")
        }
    }
}


