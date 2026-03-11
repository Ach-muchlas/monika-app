package com.sss.monikaapps.feature.download.domain.repository

import android.content.Context
import com.sss.monikaapps.common.constanta.TableIdConstant.CUSTOMER_INVOICE
import com.sss.monikaapps.common.constanta.TableIdConstant.NOTA_INVOICE
import com.sss.monikaapps.common.constanta.TableIdConstant.REASON_INVOICE
import com.sss.monikaapps.common.constanta.TableIdConstant.VISIT
import com.sss.monikaapps.common.constanta.TableNameConstant.CUSTOMER_INVOICE_TABLE
import com.sss.monikaapps.common.constanta.TableNameConstant.NOTA_INVOICE_TABLE
import com.sss.monikaapps.common.constanta.TableNameConstant.REASON_INVOICE_TABLE
import com.sss.monikaapps.common.constanta.TableNameConstant.VISIT_TABLE
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

    override suspend fun fetchDownload(
        onProgress: (Float) -> Unit,
    ): Result<DownloadDataResponse> {
        return try {
            initializeDownload(onProgress)

            val visitData = fetchAndSaveVisitData(onProgress)
                ?: return Result.error(null, "Data kosong dari server")

            val invoiceCustomerData = fetchAndSaveCustomerInvoice(onProgress)

            val notaInvoiceData = fetchAndSaveNotaInvoice(onProgress)

            val reason = fetchAndSaveReasonInvoice(onProgress)

            Result.success(
                DownloadDataResponse(
                    visit = visitData,
                    customerInvoice = invoiceCustomerData,
                    notaInvoice = notaInvoiceData,
                    reason = reason
                )
            )
        } catch (e: Exception) {
            onProgress(0f)
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
        )
    }

    override fun getInvoiceCount(): Flow<Int> = localInvoice.getInvoiceCount()
    override fun getVisitCount(): Flow<Int> = local.countVisit()


    private suspend fun initializeDownload(onProgress: (Float) -> Unit) {
        onProgress(0.01f)
        local.deleteAllLocalData(context)
        onProgress(0.02f)
        StorageHelper.deleteAppStorage(context)
        onProgress(0.03f)
        StorageHelper.clearAppCache(context)
        onProgress(0.04f)
        local.insertConfigDownload(listTableConfig())
        onProgress(0.05f)
    }

    private suspend fun fetchAndSaveVisitData(
        onProgress: (Float) -> Unit,
    ): VisitDownloadResponse? {
        onProgress(0.05f)

        val visitData = remote.fetchDownloadVisit()
            ?: return null

        val entities = serverToEntity(visitData.data ?: emptyList())

        local.insertCustomerVisitBatch(entities) { insertProgress ->
            onProgress(0.05f + insertProgress * 0.35f)
        }

        val totalLocal = local.countVisit().firstOrNull() ?: 0
        val isValid = remote.checkDataDownloadVisit(totalLocal) == "1"

        if (isValid) {
            updateConfigStatus(VISIT, VISIT_TABLE, totalLocal, visitData.totalData ?: 0)
        } else {
            local.deleteVisit()
            local.returnDataVisitCustomerConfigDownload(VISIT_TABLE)
            onProgress(0f)
            throw IllegalStateException("Jumlah data kunjungan tidak cocok. Local = $totalLocal, Server = ${visitData.totalData}")
        }

        return visitData
    }

    private suspend fun fetchAndSaveCustomerInvoice(
        onProgress: (Float) -> Unit,
    ): CustomerInvoiceResponse? {
        onProgress(0.40f)

        val invoiceData = remoteInvoice.getCustomerInvoice()

        val entities = invoiceData?.data?.map { it.toEntity() } ?: emptyList()
        localInvoice.insertCustomerInvoiceBatch(entities) { insertProgress ->
            onProgress(0.40f + insertProgress * 0.20f)
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
            onProgress(0f)
            throw IllegalStateException("Jumlah data customer tagihan tidak cocok. Local = $totalLocal, Server = ${invoiceData?.totalData}")
        }

        return invoiceData
    }

    private suspend fun fetchAndSaveNotaInvoice(
        onProgress: (Float) -> Unit,
    ): NotaInvoiceResponse? {
        onProgress(0.60f)

        val notaData = remoteInvoice.getNotaInvoice()

        val entities = notaData?.data?.map { it.toEntity() } ?: emptyList()
        localInvoice.insertNotaInvoiceBatch(entities) { insertProgress ->
            onProgress(0.60f + insertProgress * 0.20f)
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
            onProgress(0f)
            throw IllegalStateException(
                "Jumlah data nota tagihan tidak cocok. Local = $totalLocal, Server = ${notaData?.totalData}"
            )
        }

        return notaData
    }

    private suspend fun fetchAndSaveReasonInvoice(
        onProgress: (Float) -> Unit,
    ): ReasonInvoiceResponse? {
        onProgress(0.80f)

        val reasonData = remoteInvoice.getReasonInvoice()

        val entities = reasonData?.data?.map { it.toEntity() } ?: emptyList()
        localInvoice.insertReasonInvoiceBatch(entities) { insertProgress ->
            onProgress(0.80f + insertProgress * 0.20f)
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
            onProgress(0f)
            throw IllegalStateException(
                "Jumlah data alasan tagihan tidak cocok. Local = $totalLocal, Server = ${reasonData?.totalData}"
            )
        }

        return reasonData
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