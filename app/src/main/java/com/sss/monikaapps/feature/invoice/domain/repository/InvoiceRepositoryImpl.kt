package com.sss.monikaapps.feature.invoice.domain.repository

import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.local.InvoiceLocalDataSource
import kotlinx.coroutines.flow.Flow

class InvoiceRepositoryImpl(private val local: InvoiceLocalDataSource) : InvoiceRepository {
    override fun getCustomerInvoice(): Flow<List<CustomerInvoiceEntity>> =
        local.getCustomerInvoice()

    override fun searchCustomerInvoice(query: String): Flow<List<CustomerInvoiceEntity>> =
        local.searchCustomerInvoice(query)


}