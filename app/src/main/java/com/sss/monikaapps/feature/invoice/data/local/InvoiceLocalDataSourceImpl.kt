package com.sss.monikaapps.feature.invoice.data.local

import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDateTime
import com.sss.monikaapps.common.helper.MapsHelper
import com.sss.monikaapps.feature.activity.data.response.PhotoItem
import com.sss.monikaapps.feature.invoice.data.dao.InvoiceDao
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceData
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceRequestDataLocal
import com.sss.monikaapps.feature.invoice.domain.model.PhotoPaymentInvoice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

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

    override suspend fun insertReasonInvoiceBatch(
        data: List<ReasonEntity>,
        onProgress: (Float) -> Unit,
    ) {
        val total = data.size
        var inserted = 0

        data.chunked(500).forEach { batch ->
            dao.insertReasonInvoiceBatch(batch)

            inserted += batch.size
            val progress = inserted.toFloat() / total.toFloat()
            onProgress(progress)
        }
    }

    override suspend fun countCustomerInvoice(): Int = dao.countCustomerInvoice()
    override suspend fun countNotaInvoice(): Int = dao.countNotaInvoice()
    override suspend fun countReasonInvoice(): Int = dao.countReasonInvoice()


    override fun getInvoiceCount(): Flow<Int> = dao.observeInvoiceCount()
    override fun getCustomerInvoice(): Flow<List<CustomerInvoiceEntity>> =
        dao.observeCustomerInvoice()

    override fun getCustomerInvoiceWithFilter(
        query: String,
        status: Int,
    ): Flow<List<CustomerInvoiceEntity>> = dao.observeFilteredInvoice(query, status)

    override fun searchCustomerInvoice(query: String): Flow<List<CustomerInvoiceEntity>> =
        dao.searchCustomerInvoice(query)

    override fun getCustomerInvoiceByCustomerId(customerId: String): Flow<CustomerInvoiceEntity> =
        dao.observeCustomerInvoiceByCustomerId(customerId)

    override fun getNotaInvoiceByCustomerId(customerId: String): Flow<List<InvoiceEntity>> =
        dao.observerNotaInvoiceByCustomerId(customerId)

    override fun getPhotoNotaByIdNota(idNota: String): Flow<List<PhotoItem>> =
        dao.observerPhotoNota(idNota)

    override fun getPaymentDataInvoice(nota: String): Flow<PaymentInvoiceData> =
        dao.observerPaymentDataInvoice(nota)

    override fun getReasonInvoice(): Flow<List<ReasonEntity>> = dao.observeReasonInvoice()

    override suspend fun submitPaymentInvoice(
        nota: String,
        customerId: String,
        moneyPaid: Long,
        status: Int,
        reasonId: Int,
        descReason: String,
        gpsLat: String,
        gpsLng: String,
    ): Int {
        // get data customer
        val customer = dao.observeCustomerInvoiceByCustomerId(customerId).first()
        // calculate and format distance
        val distanceDifference = MapsHelper.calculateFormattedDistance(
            customer.gpsLatCustomer,
            customer.gpsLngCustomer,
            gpsLat,
            gpsLng
        )
        return dao.paidInvoice(
            nota, customerId, moneyPaid, status,
            reasonId, descReason, getCurrentDateTime(), gpsLat, gpsLng, distanceDifference
        )
    }

    override suspend fun getPaymentInvoiceRequest(
        nota: String,
        customerId: String,
    ): PaymentInvoiceRequestDataLocal = dao.observerPaymentRequest(nota, customerId)


    override suspend fun getPhotoPaymentInvoice(idNota: String): List<PhotoPaymentInvoice> =
        dao.getPhotoPaymentInvoice(idNota)


    override suspend fun clearCustomerInvoice() = dao.clearCustomerInvoice()
    override suspend fun clearNotaInvoice() = dao.clearNotaInvoice()
    override suspend fun clearReasonInvoice() = dao.clearReasonInvoice()

    override suspend fun markUpIsSync(nota: String, customerId: String) =
        dao.markIsSync(nota, customerId)

    override fun countInvoiceNotSync(): Flow<Int> = dao.countDataNotSync()

    override fun countInvoicePending(): Flow<Int> = dao.countDataPending()
    override suspend fun getPaymentInvoiceNotSync(): List<PaymentInvoiceRequestDataLocal> =
        dao.observerPaymentNotSync()

}