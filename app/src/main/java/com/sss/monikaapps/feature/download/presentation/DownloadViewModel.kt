package com.sss.monikaapps.feature.download.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDate
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.data.model.DownloadDataResponse
import com.sss.monikaapps.feature.download.domain.usecase.DownloadUseCase
import com.sss.monikaapps.feature.download.domain.usecase.FetchConfigDownloadUseCase
import com.sss.monikaapps.feature.download.domain.usecase.InsertDownloadUseCase
import com.sss.monikaapps.feature.home.domain.usecase.CheckPendingDataDownloadUseCase
import com.sss.monikaapps.feature.home.domain.usecase.GetCountInvoicePendingUseCase
import com.sss.monikaapps.feature.home.domain.usecase.ListTableConfigUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val downloadUseCase: DownloadUseCase,
    private val fetchConfigDownloadUseCase: FetchConfigDownloadUseCase,
    private val insertConfig: InsertDownloadUseCase,
    private val listTableConfig: ListTableConfigUseCase,
    private val pendingDownload: CheckPendingDataDownloadUseCase,
    private val getCountInvoicePendingUseCase: GetCountInvoicePendingUseCase,
) : ViewModel() {
    private val _downloadResult = MutableLiveData<Result<DownloadDataResponse>>()
    val downloadResult: LiveData<Result<DownloadDataResponse>> = _downloadResult

    private val _configDownloadResult = MutableLiveData<Result<List<ConfigDownloadDataEntity>>>()
    val configDownloadResult: LiveData<Result<List<ConfigDownloadDataEntity>>> =
        _configDownloadResult

    private val _messageEvent = MutableSharedFlow<String>()
    val messageEvent = _messageEvent.asSharedFlow()


    fun fetchConfigDownload() {
        viewModelScope.launch {
            _configDownloadResult.value = Result.loading(null)
            _configDownloadResult.value = fetchConfigDownloadUseCase()
        }
    }

    fun fetchDownload() {
        viewModelScope.launch {
            _downloadResult.value = Result.loading(null, 0f)

            val result = downloadUseCase.execute { progress ->
                _downloadResult.value = Result.loading(null, progress)
            }

            _downloadResult.value = result
        }
    }

    fun checkAndFetchDownload() {
        viewModelScope.launch {
            val currentDate = getCurrentDate()
            val configData = fetchConfigDownloadUseCase()

            val invoicePendingResult = getCountInvoicePendingUseCase().first()
            val totalInvoicePending = invoicePendingResult.first + invoicePendingResult.second

            if (totalInvoicePending != 0) {
                _messageEvent.emit("Masih ada $totalInvoicePending data tagihan yang belum diselesaikan, selesaikan terlebih dahulu sebelum melakukan download")
                return@launch
            }

            val lastDownloadDate = configData.data?.firstOrNull()?.createAd ?: ""

            if (lastDownloadDate != currentDate) {
                fetchDownload()
            } else {
                // Hari yang sama -> Cek pending
                val pending = pendingDownload().data ?: 0
                if (pending > 0) {
                    fetchDownload()
                } else {
                    _messageEvent.emit("Semua data sudah terdownload untuk hari ini")
                }
            }
        }
    }

    fun initConfigIfEmpty() {
        viewModelScope.launch {
            val defaultConfig = listTableConfig.invoke()
            insertConfig(defaultConfig)
            fetchConfigDownload()
        }
    }
}
