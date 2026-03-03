package com.sss.monikaapps.feature.invoice.domain.repository

import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import kotlinx.coroutines.flow.Flow

interface InvoiceRepository {
    fun getCustomerInvoice() : Flow<List<CustomerInvoiceEntity>>
    fun searchCustomerInvoice(query: String) : Flow<List<CustomerInvoiceEntity>>
}