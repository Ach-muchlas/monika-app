package com.sss.monikaapps.feature.download.domain.usecase

import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.domain.repository.DownloadRepository

class InsertDownloadUseCase(private val repository: DownloadRepository) {
    suspend operator fun invoke(
        data: List<ConfigDownloadDataEntity>,
    ) {
        return repository.insertDownloadData(data)
    }
}