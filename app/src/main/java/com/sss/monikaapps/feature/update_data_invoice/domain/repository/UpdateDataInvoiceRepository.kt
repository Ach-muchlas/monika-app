package com.sss.monikaapps.feature.update_data_invoice.domain.repository

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.update_data_invoice.data.model.UpdateDataInvoiceResponse

interface UpdateDataInvoiceRepository {
    suspend fun fetchUpdateDataInvoice(onProgress: (Float, String) -> Unit): Result<UpdateDataInvoiceResponse>
}