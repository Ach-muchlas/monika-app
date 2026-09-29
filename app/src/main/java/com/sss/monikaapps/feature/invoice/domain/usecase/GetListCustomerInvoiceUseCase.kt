package com.sss.monikaapps.feature.invoice.domain.usecase

import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow

class GetListCustomerInvoiceUseCase(private val repository: InvoiceRepository) {
    operator fun invoke(query: String, status: Int): Flow<List<CustomerInvoiceEntity>> {
        return repository.getCustomerInvoiceWithFilter(query, status)
    }
}