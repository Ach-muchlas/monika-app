package com.sss.monikaapps.feature.download.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.data.model.DownloadDataResponse
import com.sss.monikaapps.feature.download.domain.usecase.DownloadUseCase
import com.sss.monikaapps.feature.download.domain.usecase.FetchConfigDownloadUseCase
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val downloadUseCase: DownloadUseCase,
    private val fetchConfigDownloadUseCase: FetchConfigDownloadUseCase,
) : ViewModel() {
    private val _downloadResult = MutableLiveData<Result<DownloadDataResponse>>()
    val downloadResult: LiveData<Result<DownloadDataResponse>> = _downloadResult

    private val _configDownloadResult = MutableLiveData<Result<List<ConfigDownloadDataEntity>>>()
    val configDownloadResult: LiveData<Result<List<ConfigDownloadDataEntity>>> =
        _configDownloadResult

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
}
