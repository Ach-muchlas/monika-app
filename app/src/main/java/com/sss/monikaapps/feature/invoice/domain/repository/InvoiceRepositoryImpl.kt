package com.sss.monikaapps.feature.invoice.domain.repository

import com.sss.monikaapps.common.constanta.InvoiceStatusPayment.NOT_PAID
import com.sss.monikaapps.common.constanta.InvoiceStatusPayment.RECEIPT
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.local.DownloadLocalDataSource
import com.sss.monikaapps.feature.invoice.data.entity.BankReceiptEntity
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity
import com.sss.monikaapps.feature.invoice.data.local.InvoiceLocalDataSource
import com.sss.monikaapps.feature.invoice.data.remote.InvoiceRemoteDataSource
import com.sss.monikaapps.feature.invoice.data.response.GeneratePdfResponse
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
    private val localConfig: DownloadLocalDataSource,
) : InvoiceRepository {
    override fun getCustomerInvoiceWithFilter(
        query: String,
        status: Int,
    ): Flow<List<CustomerInvoiceEntity>> = local.getCustomerInvoiceWithFilter(query, status)

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
        dateReceipt: String,
        paymentMethod : String,
        idCoa : String,
    ): Int = local.submitPaymentInvoice(
        nota, customerId, moneyPaid, status, reasonId, descReason, gpsLat, gpsLng, dateReceipt, paymentMethod, idCoa
    )

    override suspend fun getDateConfig(): String = localConfig.getDateConfig()

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

    override suspend fun generatePdfInvoice(date: String): Result<GeneratePdfResponse> {
        return try {
            val response = remote.getDataInvoiceMakeGeneratePdf(date)

            if (response != null && response.totalData != 0) {
                Result.success(response)
            } else {
                Result.error(null, response?.message.toString())
            }
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred")
        }
    }

    override fun fetchBankReceipt(): Flow<List<BankReceiptEntity>> = local.getBankReceipt()

    private suspend fun getPaymentInvoiceDataLocal(
        nota: String,
        customerId: String,
    ): PaymentInvoiceRequest {
        val dataLocal = local.getPaymentInvoiceRequest(nota, customerId)
        val parentFeature = if (dataLocal.isStatus == 2) NOT_PAID else RECEIPT
        val photosLocal = local.getPhotoPaymentInvoice(dataLocal.idNota, parentFeature)

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
            dueDate = dataLocal.dueDate,
            dateNota = dataLocal.dateNota,
            amount = dataLocal.amount,
            dateReceipt = dataLocal.dateReceipt,
            paymentMethod = dataLocal.paymentMethod,
            idCoa = dataLocal.idCoa,
            photos = photosLocal.map { File(it.filePath) },
            createdAtPhotos = photosLocal.map { it.createdAt },
        )
    }
}