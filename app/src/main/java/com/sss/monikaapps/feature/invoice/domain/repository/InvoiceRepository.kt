package com.sss.monikaapps.feature.invoice.domain.repository

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity
import com.sss.monikaapps.feature.invoice.domain.model.DetailInvoiceData
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceData
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceRequest
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceRequestDataLocal
import kotlinx.coroutines.flow.Flow

interface InvoiceRepository {
    fun getCustomerInvoiceWithFilter(query: String, status: Int): Flow<List<CustomerInvoiceEntity>>

    fun getCustomerInvoice(): Flow<List<CustomerInvoiceEntity>>
    fun searchCustomerInvoice(query: String): Flow<List<CustomerInvoiceEntity>>
    fun getDetailInvoice(customerId: String): Flow<DetailInvoiceData>

    fun getPaymentDataInvoice(nota: String): Flow<PaymentInvoiceData>

    fun getInvoiceReason(): Flow<List<ReasonEntity>>

    suspend fun submitPaymentInvoiceLocal(
        nota: String,
        customerId: String,
        moneyPaid: Long,
        status: Int,
        reasonId: Int,
        descReason: String,
        gpsLat: String,
        gpsLng: String,
    ): Int

    suspend fun submitPaymentInvoiceRemote(nota: String, customerId: String): Result<String>

    fun countInvoiceNotSync(): Flow<Int>
    fun countInvoicePending(): Flow<Int>


    suspend fun getPaymentInvoiceNotSync() : List<PaymentInvoiceRequestDataLocal>
}