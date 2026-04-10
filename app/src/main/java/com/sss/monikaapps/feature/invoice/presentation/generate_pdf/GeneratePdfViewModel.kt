package com.sss.monikaapps.feature.invoice.presentation.generate_pdf

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.helper.PdfInvoiceHelper
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.domain.usecase.FetchConfigDownloadUseCase
import com.sss.monikaapps.feature.home.domain.usecase.GetCountInvoicePendingUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.GetDataGeneratePdfUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GeneratePdfViewModel(
    private val getDataGeneratePdfUseCase: GetDataGeneratePdfUseCase,
    private val fetchConfigDownloadUseCase: FetchConfigDownloadUseCase,
    private val getCountInvoicePendingUseCase: GetCountInvoicePendingUseCase,
) : ViewModel() {
    private val sessionManager = SessionManager.getInstance()
    private val _pdfResult = MutableStateFlow<Result<String>?>(null)
    val pdfResult = _pdfResult.asStateFlow()


    fun generatePdf(context: Context, currentDate: String) {
        viewModelScope.launch {
            val configData = fetchConfigDownloadUseCase()
            val downloadDate = configData.data?.firstOrNull()?.createAd ?: ""

            val invoicePendingResult = getCountInvoicePendingUseCase().first()
            val totalInvoicePending = invoicePendingResult.first + invoicePendingResult.second

            if (downloadDate == currentDate && totalInvoicePending != 0) {
                _pdfResult.value = Result.error(
                    null,
                    "Masih ada $totalInvoicePending data tagihan yang belum diselesaikan, selesaikan terlebih dahulu sebelum generate pdf hasil tagihan hari ini"
                )
                return@launch
            }

            _pdfResult.value = Result.loading(null)

            val result = getDataGeneratePdfUseCase(currentDate)

            if (result.status == StatusNetwork.SUCCESS) {
                val invoiceData = result.data?.data?.filterNotNull() ?: emptyList()

                if (result.data?.totalData == 0) {
                    _pdfResult.value = Result.error(null, "Tidak ada data untuk dibuat PDF")
                    return@launch
                }

                val file = PdfInvoiceHelper.generateMonitoringPdf(
                    context = context,
                    data = invoiceData,
                    date = currentDate,
                    collName = sessionManager.getDataUser().employeeName ?: "-"
                )

                if (file != null) {
                    _pdfResult.value = Result.success(file.absolutePath)
                } else {
                    _pdfResult.value = Result.error(null, "Gagal membuat file PDF")
                }
            } else {
                _pdfResult.value = Result.error(null, result.message ?: "Gagal mengambil data PDF")
            }
        }
    }

    fun clearPdfState() {
        _pdfResult.value = null
    }
}