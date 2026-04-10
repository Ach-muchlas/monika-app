package com.sss.monikaapps.feature.invoice.data.remote

import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.invoice.data.response.CustomerInvoiceResponse
import com.sss.monikaapps.feature.invoice.data.response.GeneratePdfResponse
import com.sss.monikaapps.feature.invoice.data.response.NotaInvoiceResponse
import com.sss.monikaapps.feature.invoice.data.response.ReasonInvoiceResponse
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceRequest

interface InvoiceRemoteDataSource {
    suspend fun getCustomerInvoice(): CustomerInvoiceResponse?
    suspend fun checkCustomerInvoice(totalData: Int): String?
    suspend fun getNotaInvoice(): NotaInvoiceResponse?
    suspend fun checkNotaInvoice(totalData: Int): String?

    suspend fun getReasonInvoice(): ReasonInvoiceResponse?
    suspend fun checkReasonInvoice(totalData: Int): String?

    suspend fun submitInvoice(payload: PaymentInvoiceRequest): DefaultAddResponse?

    suspend fun getDataInvoiceMakeGeneratePdf(date: String): GeneratePdfResponse?
}
