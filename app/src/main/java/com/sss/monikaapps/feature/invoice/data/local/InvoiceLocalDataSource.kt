package com.sss.monikaapps.feature.invoice.data.local

import com.sss.monikaapps.feature.activity.data.response.PhotoItem
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceData
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceRequestDataLocal
import com.sss.monikaapps.feature.invoice.domain.model.PhotoPaymentInvoice
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

    suspend fun insertReasonInvoiceBatch(
        data: List<ReasonEntity>,
        onProgress: (Float) -> Unit,
    )

    suspend fun countCustomerInvoice(): Int
    suspend fun countNotaInvoice(): Int
    suspend fun countReasonInvoice(): Int

    fun getInvoiceCount(): Flow<Int>

    fun getCustomerInvoice(): Flow<List<CustomerInvoiceEntity>>
    fun getCustomerInvoiceWithFilter(query : String, status : Int): Flow<List<CustomerInvoiceEntity>>
    fun searchCustomerInvoice(query: String): Flow<List<CustomerInvoiceEntity>>
    fun getCustomerInvoiceByCustomerId(customerId: String): Flow<CustomerInvoiceEntity>
    fun getNotaInvoiceByCustomerId(customerId: String): Flow<List<InvoiceEntity>>
    fun getPhotoNotaByIdNota(idNota: String): Flow<List<PhotoItem>>

    fun getPaymentDataInvoice(nota: String): Flow<PaymentInvoiceData>

    fun getReasonInvoice(): Flow<List<ReasonEntity>>

    suspend fun submitPaymentInvoice(
        nota: String,
        customerId: String,
        moneyPaid: Long,
        status: Int,
        reasonId: Int,
        descReason: String,
        gpsLat: String,
        gpsLng: String,
    ): Int

    suspend fun getPaymentInvoiceRequest(
        nota: String,
        customerId: String,
    ): PaymentInvoiceRequestDataLocal

    suspend fun getPhotoPaymentInvoice(idNota: String): List<PhotoPaymentInvoice>

    suspend fun clearCustomerInvoice()
    suspend fun clearNotaInvoice()
    suspend fun clearReasonInvoice()

    suspend fun markUpIsSync(nota: String, customerId: String)

    fun countInvoiceNotSync(): Flow<Int>
    fun countInvoicePending(): Flow<Int>

    suspend fun getPaymentInvoiceNotSync(): List<PaymentInvoiceRequestDataLocal>
}
