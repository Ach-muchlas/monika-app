package com.sss.monikaapps.feature.invoice.domain.usecase

import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepository

class SearchListCustomerInvoiceUseCase(private val repository: InvoiceRepository) {
    operator fun invoke(query: String) = repository.getCustomerInvoice()
}