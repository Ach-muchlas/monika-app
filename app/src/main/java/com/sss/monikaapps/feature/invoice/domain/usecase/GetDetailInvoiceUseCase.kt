package com.sss.monikaapps.feature.invoice.domain.usecase

import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepository

class GetDetailInvoiceUseCase(private val repository: InvoiceRepository) {
    operator fun invoke(customerId: String) = repository.getDetailInvoice(customerId)
}