package com.sss.monikaapps.feature.home.domain.usecase

import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCountInvoicePendingUseCase(private val repository: InvoiceRepository) {
    operator fun invoke(): Flow<Pair<Int, Int>> {
        return combine(
            repository.countInvoiceNotSync(),
            repository.countInvoicePending()
        ) { notSync, pending ->
            Pair(notSync, pending)
        }
    }
}