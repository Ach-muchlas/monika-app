package com.sss.monikaapps.feature.download.domain.usecase

import com.sss.monikaapps.feature.download.domain.repository.DownloadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCountInvoiceAndVisitUseCase(private val repository: DownloadRepository) {
    operator fun invoke(): Flow<Pair<Int, Int>> {
        return combine(
            repository.getInvoiceCount(),
            repository.getVisitCount()
        ) { invoiceCount, visitCount ->
            Pair(invoiceCount, visitCount)
        }
    }
}