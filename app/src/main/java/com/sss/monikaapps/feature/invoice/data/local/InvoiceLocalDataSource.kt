package com.sss.monikaapps.feature.invoice.data.local

import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity
import kotlinx.coroutines.flow.Flow

interface InvoiceLocalDataSource {
    suspend fun insertCustomerInvoiceBatch(
        data: List<CustomerInvoiceEntity>,
        onProgress: (Float) -> Unit,
    )

    suspend fun insertNotaInvoiceBatch(
        data: List<InvoiceEntity>,
        onProgress: (Float) -> Unit,
    )

    suspend fun countCustomerInvoice(): Int
    suspend fun countNotaInvoice(): Int

    fun getInvoiceCount(): Flow<Int>

    fun getCustomerInvoice(): Flow<List<CustomerInvoiceEntity>>
    fun searchCustomerInvoice(query: String): Flow<List<CustomerInvoiceEntity>>

    suspend fun clearCustomerInvoice()
    suspend fun clearNotaInvoice()
}
