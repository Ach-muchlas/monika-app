package com.sss.monikaapps.feature.home.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.domain.repository.DownloadRepository

class ListTableConfigUseCase(private val repository: DownloadRepository) {
    suspend operator fun invoke(): List<ConfigDownloadDataEntity> {
        return repository.listTableConfig()
    }
}