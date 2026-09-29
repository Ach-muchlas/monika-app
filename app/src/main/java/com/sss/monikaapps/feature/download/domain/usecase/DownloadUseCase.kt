package com.sss.monikaapps.feature.download.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.model.DownloadDataResponse
import com.sss.monikaapps.feature.download.domain.repository.DownloadRepository

class DownloadUseCase(private val repository: DownloadRepository) {
    suspend fun execute(
        onProgress: (Float, String) -> Unit,
    ): Result<DownloadDataResponse> {
        return repository.fetchDownload(onProgress)
    }
}