package com.sss.monikaapps.feature.download.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.domain.repository.DownloadRepository
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse

class DownloadUseCase(private val repository: DownloadRepository) {
    suspend fun execute(
        onProgress: (Float) -> Unit,
    ): Result<VisitDownloadResponse> {
        return repository.fetchDownload(onProgress)
    }
}