package com.sss.monikaapps.feature.invoice.domain.usecase

import com.sss.monikaapps.feature.invoice.domain.repository.InvoiceRepository

class GetDataGeneratePdfUseCase(
    private val repository: InvoiceRepository,

) {
    suspend operator fun invoke(date: String) = repository.generatePdfInvoice(date)
}