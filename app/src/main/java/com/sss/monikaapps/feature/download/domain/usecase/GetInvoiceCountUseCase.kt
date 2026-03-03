package com.sss.monikaapps.feature.download.domain.usecase

import com.sss.monikaapps.feature.download.domain.repository.DownloadRepository
import kotlinx.coroutines.flow.Flow

class GetInvoiceCountUseCase(private val repository: DownloadRepository) {
    operator fun invoke(): Flow<Int> = repository.getInvoiceCount()
}