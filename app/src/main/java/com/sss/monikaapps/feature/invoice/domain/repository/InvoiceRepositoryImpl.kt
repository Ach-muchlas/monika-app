package com.sss.monikaapps.feature.invoice.domain.repository

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity
import com.sss.monikaapps.feature.invoice.data.local.InvoiceLocalDataSource
import com.sss.monikaapps.feature.invoice.data.remote.InvoiceRemoteDataSource
import com.sss.monikaapps.feature.invoice.domain.model.DetailInvoiceData
import com.sss.monikaapps.feature.invoice.domain.model.NotaWithPhotos
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceData
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceRequest
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceRequestDataLocal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.File

class InvoiceRepositoryImpl(
    private val local: InvoiceLocalDataSource,
    private val remote: InvoiceRemoteDataSource,
) : InvoiceRepository {
    override fun getCustomerInvoiceWithFilter(
        query: String,
        status: Int,
    ): Flow<List<CustomerInvoiceEntity>> = local.getCustomerInvoiceWithFilter(query, status)

    override fun getCustomerInvoice(): Flow<List<CustomerInvoiceEntity>> =
        local.getCustomerInvoice()

    override fun searchCustomerInvoice(query: String): Flow<List<CustomerInvoiceEntity>> =
        local.searchCustomerInvoice(query)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getDetailInvoice(customerId: String): Flow<DetailInvoiceData> {
        return combine(
            local.getCustomerInvoiceByCustomerId(customerId),
            local.getNotaInvoiceByCustomerId(customerId).flatMapLatest { listNota ->
                if (listNota.isEmpty()) flowOf(emptyList())
                else {
                    val notaWithPhotosFlows = listNota.map { nota ->
                        local.getPhotoNotaByIdNota(nota.id).map { photos ->
                            NotaWithPhotos(nota, photos)
                        }
                    }
                    combine(notaWithPhotosFlows) { it.toList() }
                }
            }) { customer, listNotaWithPhotos ->
            DetailInvoiceData(customer, listNotaWithPhotos)
        }
    }

    override fun getPaymentDataInvoice(nota: String): Flow<PaymentInvoiceData> =
        local.getPaymentDataInvoice(nota)

    override fun getInvoiceReason(): Flow<List<ReasonEntity>> = local.getReasonInvoice()

    override suspend fun submitPaymentInvoiceLocal(
        nota: String,
        customerId: String,
        moneyPaid: Long,
        status: Int,
        reasonId: Int,
        descReason: String,
        gpsLat: String,
        gpsLng: String,
    ): Int = local.submitPaymentInvoice(
        nota, customerId, moneyPaid, status, reasonId, descReason, gpsLat, gpsLng
    )

    override suspend fun submitPaymentInvoiceRemote(
        nota: String,
        customerId: String,
    ): Result<String> {
        return try {
            val request = getPaymentInvoiceDataLocal(nota, customerId)
            val response = remote.submitInvoice(request)

            if (response != null && response.data == "1") {
                local.markUpIsSync(nota, customerId)
                Result.success(response.message ?: "Berhasil mengirim data tagihan ke server")
            } else {
                Result.error(null, response?.message ?: "Gagal sinkron: Respon server tidak valid")
            }
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred")
        }
    }

    override fun countInvoiceNotSync(): Flow<Int> = local.countInvoiceNotSync()

    override fun countInvoicePending(): Flow<Int> = local.countInvoicePending()


    override suspend fun getPaymentInvoiceNotSync(): List<PaymentInvoiceRequestDataLocal> =
        local.getPaymentInvoiceNotSync()

    private suspend fun getPaymentInvoiceDataLocal(
        nota: String,
        customerId: String,
    ): PaymentInvoiceRequest {
        val dataLocal = local.getPaymentInvoiceRequest(nota, customerId)
        val photosLocal = local.getPhotoPaymentInvoice(dataLocal.idNota)

        return PaymentInvoiceRequest(
            idMobile = dataLocal.idNota,
            nomorNota = dataLocal.nomorNota,
            customerId = dataLocal.customerId,
            customerName = dataLocal.customerName,
            customerAddress = dataLocal.customerAddress,
            customerPhone = dataLocal.customerPhone,
            customerLat = dataLocal.customerLat,
            customerLng = dataLocal.customerLng,
            userLat = dataLocal.userLat,
            userLng = dataLocal.userLng,
            isStatus = dataLocal.isStatus,
            entryTime = dataLocal.entryTime,
            outstandingNota = dataLocal.outstandingNota,
            payment = dataLocal.payment,
            dateDownload = dataLocal.dateDownload,
            idReason = dataLocal.idReason,
            descReason = dataLocal.descReason,
            distanceDifference = dataLocal.distanceDifference,
            photos = photosLocal.map { File(it.filePath) },
            createdAtPhotos = photosLocal.map { it.createdAt },
        )
    }

}