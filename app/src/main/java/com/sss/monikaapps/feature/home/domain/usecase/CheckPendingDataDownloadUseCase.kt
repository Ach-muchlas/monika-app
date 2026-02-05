package com.sss.monikaapps.feature.home.domain.usecase

import androidx.lifecycle.ViewModel
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.domain.repository.DownloadRepository

class CheckPendingDataDownloadUseCase(private val repository: DownloadRepository) : ViewModel() {
    suspend operator fun invoke(): Result<Int> {
        return try {
            val result = repository.countDataPending()
            Result.success(result)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!!")
        }
    }
}