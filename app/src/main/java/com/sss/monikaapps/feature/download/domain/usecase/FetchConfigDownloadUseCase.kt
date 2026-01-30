package com.sss.monikaapps.feature.download.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.domain.repository.DownloadRepository

class FetchConfigDownloadUseCase(private val repository: DownloadRepository) {
    suspend operator fun invoke(): Result<List<ConfigDownloadDataEntity>> {
        return repository.fetchDataConfigDownload()
    }
}