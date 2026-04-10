package com.sss.monikaapps.feature.download.domain.repository

import android.content.Context
import com.sss.monikaapps.common.constanta.TableIdConstant.BANK_RECEIPT
import com.sss.monikaapps.common.constanta.TableIdConstant.CUSTOMER_INVOICE
import com.sss.monikaapps.common.constanta.TableIdConstant.NOTA_INVOICE
import com.sss.monikaapps.common.constanta.TableIdConstant.REASON_INVOICE
import com.sss.monikaapps.common.constanta.TableIdConstant.VISIT
import com.sss.monikaapps.common.constanta.TableNameConstant.BANK_RECEIPT_TABLE
import com.sss.monikaapps.common.constanta.TableNameConstant.CUSTOMER_INVOICE_TABLE
import com.sss.monikaapps.common.constanta.TableNameConstant.NOTA_INVOICE_TABLE
import com.sss.monikaapps.common.constanta.TableNameConstant.REASON_INVOICE_TABLE
import com.sss.monikaapps.common.constanta.TableNameConstant.VISIT_TABLE
import com.sss.monikaapps.common.formatter.FormatterDate
import com.sss.monikaapps.common.helper.StorageHelper
import com.sss.monikaapps.common.mapper.MapperInvoice.toEntity
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.data.local.DownloadLocalDataSource
import com.sss.monikaapps.feature.download.data.mapper.VisitMapper.serverToEntity
import com.sss.monikaapps.feature.download.data.model.DownloadDataResponse
import com.sss.monikaapps.feature.download.data.remote.DownloadRemoteDataSource
import com.sss.monikaapps.feature.invoice.data.local.InvoiceLocalDataSource
import com.sss.monikaapps.feature.invoice.data.remote.InvoiceRemoteDataSource
import com.sss.monikaapps.feature.invoice.data.response.BankReceiptResponse
import com.sss.monikaapps.feature.invoice.data.response.CustomerInvoiceResponse
import com.sss.monikaapps.feature.invoice.data.response.NotaInvoiceResponse
import com.sss.monikaapps.feature.invoice.data.response.ReasonInvoiceResponse
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class DownloadRepositoryImpl(
    private val context: Context,
    private val remoteInvoice: InvoiceRemoteDataSource,
    private val localInvoice: InvoiceLocalDataSource,
    private val remote: DownloadRemoteDataSource,
    private val local: DownloadLocalDataSource,
) : DownloadRepository {
    override suspend fun fetchDataConfigDownload(): Result<List<ConfigDownloadDataEntity>> {
        return try {
            val data = local.fetchDataConfig()
            Result.success(data)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!!")
        }
    }

    override fun fetchDataConfig2(): Flow<List<ConfigDownloadDataEntity>> = local.fetchDataConfig2()

    override suspend fun fetchDownload(
        onProgress: (Float, String) -> Unit,
    ): Result<DownloadDataResponse> {
        return try {
            initializeDownload(onProgress)

            onProgress(0.05f, "Memvalidasi sesi download...")

            val checkResult =
                remote.checkDownloadFirst(FormatterDate.getCurrentDate())

            if (checkResult == null || checkResult.data?.isFirstDownload == false) {
                onProgress(0f, "Gagal")
                return Result.error(null, checkResult?.message ?: "Gagal memvalidasi sesi download")
            }

            onProgress(0.10f, "Sesi tervalidasi")

            // 3. Visit (0.10 - 0.30)
            val visitData = fetchAndSaveVisitData(onProgress)
                ?: return Result.error(null, "Data kunjungan kosong")

            // 4. Customer Invoice (0.30 - 0.50)
            val invoiceCustomerData = fetchAndSaveCustomerInvoice(onProgress)

            // 5. Nota Invoice (0.50 - 0.70)
            val notaInvoiceData = fetchAndSaveNotaInvoice(onProgress)

            // 6. Reason Invoice (0.70 - 0.90)
            val reason = fetchAndSaveReasonInvoice(onProgress)

            // 6. Bank Receipt (0.90 - 1.0)
            val bankReceipt = fetchAndSaveBankReceipt(onProgress)

            onProgress(1.0f, "Download Selesai")

            Result.success(
                DownloadDataResponse(
                    visit = visitData,
                    customerInvoice = invoiceCustomerData,
                    notaInvoice = notaInvoiceData,
                    reason = reason,
                    bankReceipt = bankReceipt
                )
            )
        } catch (e: Exception) {
            onProgress(0f, "Terjadi Kesalahan")
            Result.error(null, e.message ?: "Error Occurred")
        }
    }

    override suspend fun countDataPending(): Int {
        return local.countPendingDownload()
    }

    override suspend fun insertDownloadData(data: List<ConfigDownloadDataEntity>) =
        local.insertConfigDownload(data)

    override suspend fun listTableConfig(): List<ConfigDownloadDataEntity> {
        return listOf(
            ConfigDownloadDataEntity(
                id = VISIT,
                tableName = VISIT_TABLE,
                totalDataMobile = 0,
                totalDataServer = 0,
                statusTotalDownload = false
            ),
            ConfigDownloadDataEntity(
                id = CUSTOMER_INVOICE,
                tableName = CUSTOMER_INVOICE_TABLE,
                totalDataMobile = 0,
                totalDataServer = 0,
                statusTotalDownload = false
            ),
            ConfigDownloadDataEntity(
                id = NOTA_INVOICE,
                tableName = NOTA_INVOICE_TABLE,
                totalDataMobile = 0,
                totalDataServer = 0,
                statusTotalDownload = false
            ),
            ConfigDownloadDataEntity(
                id = REASON_INVOICE,
                tableName = REASON_INVOICE_TABLE,
                totalDataMobile = 0,
                totalDataServer = 0,
                statusTotalDownload = false
            ),
            ConfigDownloadDataEntity(
                id = BANK_RECEIPT,
                tableName = BANK_RECEIPT_TABLE,
                totalDataMobile = 0,
                totalDataServer = 0,
                statusTotalDownload = false
            ),
        )
    }

    override fun getInvoiceCount(): Flow<Int> = localInvoice.getInvoiceCount()
    override fun getVisitCount(): Flow<Int> = local.countVisit()


    private suspend fun initializeDownload(onProgress: (Float, String) -> Unit) {
        onProgress(0.01f, "Persiapan download")
        local.deleteAllLocalData(context)
        onProgress(0.02f, "Menghapus database")
        StorageHelper.deleteAppStorage(context)
        onProgress(0.03f, "Menghapus storage")
        StorageHelper.clearAppCache(context)
        onProgress(0.04f, "Menghapus cache")
        local.insertConfigDownload(listTableConfig())
        onProgress(0.05f, "Menginisialisasi konfigurasi download")
    }


    private suspend fun fetchAndSaveVisitData(
        onProgress: (Float, String) -> Unit,
    ): VisitDownloadResponse? {
        onProgress(0.05f, "Mengunduh data kunjungan")

        val visitData = remote.fetchDownloadVisit()
            ?: return null

        val entities = serverToEntity(visitData.data ?: emptyList())

        local.insertCustomerVisitBatch(entities) { insertProgress ->
            onProgress(0.05f + insertProgress * 0.25f, "Menyimpan data kunjungan")
        }

        val totalLocal = local.countVisit().firstOrNull() ?: 0
        val isValid = remote.checkDataDownloadVisit(totalLocal) == "1"

        if (isValid) {
            updateConfigStatus(VISIT, VISIT_TABLE, totalLocal, visitData.totalData ?: 0)
        } else {
            local.deleteVisit()
            local.returnDataVisitCustomerConfigDownload(VISIT_TABLE)
            onProgress(0f, "Gagal Mengunduh data kunjungan")
            throw IllegalStateException("Jumlah data kunjungan tidak cocok. Local = $totalLocal, Server = ${visitData.totalData}")
        }

        return visitData
    }

    private suspend fun fetchAndSaveCustomerInvoice(
        onProgress: (Float, String) -> Unit,
    ): CustomerInvoiceResponse? {
        onProgress(0.30f, "Mengunduh data tagihan pelanggan")

        val invoiceData = remoteInvoice.getCustomerInvoice()

        val entities = invoiceData?.data?.map { it.toEntity() } ?: emptyList()
        localInvoice.insertCustomerInvoiceBatch(entities) { insertProgress ->
            onProgress(0.30f + insertProgress * 0.20f, "Menyimpan data tagihan pelanggan")
        }

        val totalLocal = localInvoice.countCustomerInvoice()
        val isValid = remoteInvoice.checkCustomerInvoice(totalLocal) == "1"

        if (isValid) {
            updateConfigStatus(
                CUSTOMER_INVOICE,
                CUSTOMER_INVOICE_TABLE,
                totalLocal,
                invoiceData?.totalData ?: 0
            )
        } else {
            localInvoice.clearCustomerInvoice()
            local.returnDataVisitCustomerConfigDownload(CUSTOMER_INVOICE_TABLE)
            onProgress(0f, "Gagal Mengunduh data tagihan pelanggan")
            throw IllegalStateException("Jumlah data customer tagihan tidak cocok. Local = $totalLocal, Server = ${invoiceData?.totalData}")
        }

        return invoiceData
    }

    private suspend fun fetchAndSaveNotaInvoice(
        onProgress: (Float, String) -> Unit,
    ): NotaInvoiceResponse? {
        onProgress(0.50f, "Mengunduh data nota tagihan")

        val notaData = remoteInvoice.getNotaInvoice()

        val entities = notaData?.data?.map { it.toEntity() } ?: emptyList()

        localInvoice.insertNotaInvoiceBatch(entities) { insertProgress ->
            onProgress(0.70f + insertProgress * 0.20f, "Menyimpan data nota tagihan")
        }

        val totalLocal = localInvoice.countNotaInvoice()
        val isValid = remoteInvoice.checkNotaInvoice(totalLocal) == "1"

        if (isValid) {
            updateConfigStatus(
                NOTA_INVOICE,
                NOTA_INVOICE_TABLE,
                totalLocal,
                notaData?.totalData ?: 0
            )
        } else {
            localInvoice.clearNotaInvoice()
            local.returnDataVisitCustomerConfigDownload(NOTA_INVOICE_TABLE)
            onProgress(0f, "Gagal mengunduh data nota tagihan")
            throw IllegalStateException(
                "Jumlah data nota tagihan tidak cocok. Local = $totalLocal, Server = ${notaData?.totalData}"
            )
        }

        return notaData
    }

    private suspend fun fetchAndSaveReasonInvoice(
        onProgress: (Float, String) -> Unit,
    ): ReasonInvoiceResponse? {
        onProgress(0.70f, "Mengunduh data alasan tagihan")

        val reasonData = remoteInvoice.getReasonInvoice()

        val entities = reasonData?.data?.map { it.toEntity() } ?: emptyList()
        localInvoice.insertReasonInvoiceBatch(entities) { insertProgress ->
            onProgress(0.70f + insertProgress * 0.20f, "Menyimpan data alasan tagihan")
        }

        val totalLocal = localInvoice.countReasonInvoice()
        val isValid = remoteInvoice.checkReasonInvoice(totalLocal) == "1"

        if (isValid) {
            updateConfigStatus(
                REASON_INVOICE,
                REASON_INVOICE_TABLE,
                totalLocal,
                reasonData?.totalData ?: 0
            )
        } else {
            localInvoice.clearReasonInvoice()
            local.returnDataVisitCustomerConfigDownload(REASON_INVOICE_TABLE)
            onProgress(0f, "Gagal mengunduh data alasan tagihan")
            throw IllegalStateException(
                "Jumlah data alasan tagihan tidak cocok. Local = $totalLocal, Server = ${reasonData?.totalData}"
            )
        }

        return reasonData
    }

    private suspend fun fetchAndSaveBankReceipt(
        onProgress: (Float, String) -> Unit,
    ): BankReceiptResponse? {
        onProgress(0.90f, "Mengunduh data bank")

        val bankReceipt = remote.fetchBankReceipt()

        val entities = bankReceipt?.data?.map { it.toEntity() } ?: emptyList()
        localInvoice.insertBankReceiptBatch(entities) { insertProgress ->
            onProgress(0.90f + insertProgress * 0.10f, "Menyimpan data bank")
        }

        val totalLocal = local.countDataBankReceipt()

        val isValid = remote.checkBankReceipt(totalLocal) == "1"

        if (isValid) {
            updateConfigStatus(
                BANK_RECEIPT,
                BANK_RECEIPT_TABLE,
                totalLocal,
                bankReceipt?.totalData ?: 0
            )
        } else {
            local.deleteDataBankReceipt()
            local.returnDataVisitCustomerConfigDownload(BANK_RECEIPT_TABLE)
            onProgress(0f, "Gagal mengunduh data bank ")
            throw IllegalStateException(
                "Jumlah data alasan tagihan tidak cocok. Local = $totalLocal, Server = ${bankReceipt?.totalData}"
            )
        }

        return bankReceipt
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