package com.sss.monikaapps.feature.update_data_invoice.domain.repository

import com.sss.monikaapps.common.mapper.MapperInvoice.toEntity
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.data.local.DownloadLocalDataSource
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity
import com.sss.monikaapps.feature.invoice.data.local.InvoiceLocalDataSource
import com.sss.monikaapps.feature.invoice.data.remote.InvoiceRemoteDataSource
import com.sss.monikaapps.feature.update_data_invoice.data.model.UpdateDataInvoiceResponse

class UpdateDataInvoiceRepositoryImpl(
    private val remoteInvoice: InvoiceRemoteDataSource,
    private val localInvoice: InvoiceLocalDataSource,
    private val local: DownloadLocalDataSource,
) : UpdateDataInvoiceRepository {

    override suspend fun fetchUpdateDataInvoice(onProgress: (Float, String) -> Unit): Result<UpdateDataInvoiceResponse> {
        return try {
            val customer = fetchAndSaveCustomerInvoiceUpdated(onProgress)

            val nota = fetchAndSaveNotaInvoiceUpdated(onProgress)

            onProgress(1.0f, "Update Data Selesai")
            Result.success(
                UpdateDataInvoiceResponse(
                    listCustomerInvoice = customer,
                    listNotaInvoice = nota
                )
            )
        } catch (e: Exception) {
            onProgress(0f, "Terjadi Kesalahan")
            Result.error(null, e.message ?: "Error Occurred")
        }
    }

    private suspend fun fetchAndSaveCustomerInvoiceUpdated(
        onProgress: (Float, String) -> Unit,
    ): List<CustomerInvoiceEntity> {

        // 0.0 → 0.2 (download)
        onProgress(0.0f, "Mengunduh data tagihan pelanggan")
        val invoiceData = remoteInvoice.getCustomerInvoice()
        onProgress(0.2f, "Download selesai")

        val existingIds = localInvoice.getCustomerIdInCustomerTable()

        val entities = invoiceData?.data
            ?.filter { it.customerId !in existingIds }
            ?.map { it.toEntity() } ?: emptyList()

        // 0.2 → 0.5 (insert)
        localInvoice.insertCustomerInvoiceBatch(entities) { insertProgress ->
            val progress = 0.2f + (insertProgress * 0.3f)
            onProgress(progress, "Menyimpan data tagihan pelanggan")
        }

        val totalLocal = localInvoice.countCustomerInvoice()
        val isValid = remoteInvoice.checkCustomerInvoice(totalLocal) == "1"

        if (!isValid) {
            localInvoice.clearCustomerInvoice()
            onProgress(0f, "Gagal Mengunduh data pelanggan")
            throw IllegalStateException("Mismatch data customer")
        }

        return entities
    }

    private suspend fun fetchAndSaveNotaInvoiceUpdated(
        onProgress: (Float, String) -> Unit,
    ): List<InvoiceEntity> {

        // 0.5 → 0.7
        onProgress(0.5f, "Mengunduh data nota")
        val notaData = remoteInvoice.getNotaInvoice()
        onProgress(0.7f, "Download nota selesai")

        val existingIds = localInvoice.getCustomerIdInInvoiceTable()

        val entities = notaData?.data
            ?.filter { it.customerId !in existingIds }
            ?.map { it.toEntity() } ?: emptyList()

        // 0.7 → 1.0
        localInvoice.insertNotaInvoiceBatch(entities) { insertProgress ->
            val progress = 0.7f + (insertProgress * 0.3f)
            onProgress(progress, "Menyimpan data nota tagihan")
        }

        val totalLocal = localInvoice.countNotaInvoice()
        val isValid = remoteInvoice.checkNotaInvoice(totalLocal) == "1"

        if (!isValid) {
            localInvoice.clearNotaInvoice()
            onProgress(0f, "Gagal Mengunduh data nota")
            throw IllegalStateException("Mismatch data nota")
        }

        return entities
    }

    private suspend fun updateConfigStatus(
        featureId: Int,
        tableName: String,
        totalLocal: Int,
        totalServer: Int,
    ) {
        local.saveConfigDownload(
            ConfigDownloadDataEntity(
                id = featureId,
                tableName = tableName,
                totalDataMobile = totalLocal,
                totalDataServer = totalServer,
                statusTotalDownload = true
            )
        )
    }
}