package com.sss.monikaapps.feature.invoice.domain.usecase

import com.sss.monikaapps.feature.invoice.data.entity.BankReceiptEntity
import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchBankReceiptUseCase(private val repository: InvoiceRepository) {
    operator fun invoke(): Flow<List<BankReceiptEntity>> {
        return repository.fetchBankReceipt().map { list ->
            list.sortedBy { it.bankName }
        }
    }
}