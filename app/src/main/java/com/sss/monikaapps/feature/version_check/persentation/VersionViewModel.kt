package com.sss.monikaapps.feature.version_check.persentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.constanta.DownloadUrlConstant.URL_DOWNLOAD_IN_OFFICE
import com.sss.monikaapps.common.constanta.DownloadUrlConstant.URL_DOWNLOAD_NON_OFFICE
import com.sss.monikaapps.common.constanta.ServerConstant.BASE_URL_OFFICE
import com.sss.monikaapps.common.manager.ServerManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class VersionViewModel() : ViewModel() {
    private val serverManager = ServerManager.getInstance()
    private val server = serverManager.getServerAddress()

    private val _openUrlEvent = MutableSharedFlow<String>()
    val openUrlEvent = _openUrlEvent.asSharedFlow()

    fun onDownloadClicked() {
        val url = if (server == BASE_URL_OFFICE)
            URL_DOWNLOAD_IN_OFFICE
        else
            URL_DOWNLOAD_NON_OFFICE

        viewModelScope.launch {
            _openUrlEvent.emit(url)
        }
    }

}
