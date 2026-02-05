package com.sss.monikaapps.feature.connection.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.connection.data.response.VersionResponse
import com.sss.monikaapps.feature.connection.domain.usecase.ChangeServerUseCase
import com.sss.monikaapps.feature.connection.domain.usecase.FetchServerUrlUseCase
import kotlinx.coroutines.launch

class ConnectionViewModel(
    private val changeServerUseCase: ChangeServerUseCase,
    private val fetchServerUrlUseCase: FetchServerUrlUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<Result<VersionResponse>>()
    val state: LiveData<Result<VersionResponse>> = _state

    private val _serverUrl = MutableLiveData<Result<String>>()
    val serverUrl: LiveData<Result<String>> = _serverUrl


    fun changeServer(url: String) {
        viewModelScope.launch {
            _state.value = Result.loading(null)

            val result = changeServerUseCase(url)
            _state.value = result
        }
    }

    fun fetchServerUrl() {
        viewModelScope.launch {
            _serverUrl.value = Result.loading(null)

            val result = fetchServerUrlUseCase()
            _serverUrl.value = result
        }
    }
}