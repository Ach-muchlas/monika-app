package com.sss.monikaapps.feature.invoice.data.remote

import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.invoice.data.response.CustomerInvoiceResponse
import com.sss.monikaapps.feature.invoice.data.response.NotaInvoiceResponse

interface InvoiceRemoteDataSource {
    suspend fun getCustomerInvoice() : CustomerInvoiceResponse?
    suspend fun checkCustomerInvoice(totalData : Int) : String?
    suspend fun getNotaInvoice() : NotaInvoiceResponse?
    suspend fun checkNotaInvoice(totalData : Int) : String?
}
