package com.sss.monikaapps.feature.invoice.domain.usecase

import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepository

class GetPaymentDataInvoiceUseCase(private val repository: InvoiceRepository) {
    operator fun invoke(nota: String) = repository.getPaymentDataInvoice(nota)
}