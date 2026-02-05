package com.sss.monikaapps.feature.utils.device.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.feature.utils.device.domain.usecase.GetAppVersionUseCase
import com.sss.monikaapps.feature.utils.device.domain.usecase.GetDeviceIdUseCase
import com.sss.monikaapps.feature.utils.device.domain.usecase.GetSystemOperationUseCase
import kotlinx.coroutines.launch

class DeviceViewModel(
    private val getDeviceIdUseCase: GetDeviceIdUseCase,
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val systemOperationUseCase: GetSystemOperationUseCase,
) : ViewModel() {
    private val _deviceId = MutableLiveData<String>()
    val deviceId: LiveData<String> = _deviceId

    private val _versionApps = MutableLiveData<String>()
    val versionApps: LiveData<String> = _versionApps

    private val _systemOperation = MutableLiveData<String>()
    val systemOperation: LiveData<String> = _systemOperation

    init {
        fetchDeviceId()
        fetchVersionApps()
        fetchSystemOperation()
    }

    private fun fetchDeviceId() {
        viewModelScope.launch {
            try {
                val id = getDeviceIdUseCase()
                _deviceId.value = id
            } catch (e: Exception) {
                _deviceId.value = "Tidak tersedia"
            }
        }
    }

    private fun fetchVersionApps() {
        viewModelScope.launch {
            try {
                val id = getAppVersionUseCase()
                _versionApps.value = id
            } catch (e: Exception) {
                _versionApps.value = "Tidak tersedia"
            }
        }
    }

    private fun fetchSystemOperation() {
        viewModelScope.launch {
            try {
                val id = systemOperationUseCase()
                _systemOperation.value = id
            } catch (e: Exception) {
                _systemOperation.value = "Tidak tersedia"
            }
        }
    }

}