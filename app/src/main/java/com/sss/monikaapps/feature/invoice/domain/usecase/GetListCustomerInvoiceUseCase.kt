package com.sss.monikaapps.feature.invoice.domain.usecase

import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepository

class GetListCustomerInvoiceUseCase(private val repository: InvoiceRepository) {
    operator fun invoke() = repository.getCustomerInvoice()
}