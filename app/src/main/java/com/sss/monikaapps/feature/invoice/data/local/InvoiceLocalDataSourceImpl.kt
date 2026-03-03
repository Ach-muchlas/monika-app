package com.sss.monikaapps.feature.invoice.data.local

import com.sss.monikaapps.feature.invoice.data.dao.InvoiceDao
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity
import kotlinx.coroutines.flow.Flow

class InvoiceLocalDataSourceImpl(private val dao: InvoiceDao) : InvoiceLocalDataSource {
    override suspend fun insertCustomerInvoiceBatch(
        data: List<CustomerInvoiceEntity>,
        onProgress: (Float) -> Unit,
    ) {
        val total = data.size
        var inserted = 0

        data.chunked(500).forEach { batch ->
            dao.insertCustomerInvoiceBatch(batch)

            inserted += batch.size
            val progress = inserted.toFloat() / total.toFloat()
            onProgress(progress)
        }
    }

    override suspend fun insertNotaInvoiceBatch(
        data: List<InvoiceEntity>,
        onProgress: (Float) -> Unit,
    ) {
        val total = data.size
        var inserted = 0

        data.chunked(500).forEach { batch ->
            dao.insertNotaInvoiceBatch(batch)

            inserted += batch.size
            val progress = inserted.toFloat() / total.toFloat()
            onProgress(progress)
        }
    }

    override suspend fun countCustomerInvoice(): Int = dao.countCustomerInvoice()
    override suspend fun countNotaInvoice(): Int = dao.countNotaInvoice()
    override fun getInvoiceCount(): Flow<Int> = dao.observeInvoiceCount()
    override fun getCustomerInvoice(): Flow<List<CustomerInvoiceEntity>> =
        dao.observeCustomerInvoice()
    override fun searchCustomerInvoice(query: String): Flow<List<CustomerInvoiceEntity>> =
        dao.searchCustomerInvoice(query)

    override suspend fun clearCustomerInvoice() = dao.clearCustomerInvoice()
    override suspend fun clearNotaInvoice() = dao.clearNotaInvoice()

}