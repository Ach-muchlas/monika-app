package com.sss.monikaapps.feature.update_data_invoice.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.update_data_invoice.data.model.UpdateDataInvoiceResponse
import com.sss.monikaapps.feature.update_data_invoice.domain.repository.UpdateDataInvoiceRepository

class UpdateDataInvoiceUseCase(private val repository: UpdateDataInvoiceRepository) {
    suspend fun execute(
        onProgress: (Float, String) -> Unit,
    ): Result<UpdateDataInvoiceResponse> {
        return repository.fetchUpdateDataInvoice(onProgress)
    }
}