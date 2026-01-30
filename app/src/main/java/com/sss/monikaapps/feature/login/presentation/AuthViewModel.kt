package com.sss.monikaapps.feature.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sss.monikaapps.common.result.Result
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.feature.login.domain.repository.AuthRepository
import com.sss.monikaapps.feature.login.data.model.LoginRequest
import com.sss.monikaapps.feature.login.data.response.LoginResponse
import com.sss.monikaapps.feature.login.domain.usecase.LoginUseCase
import com.sss.monikaapps.feature.utils.device.domain.usecase.GetAppVersionUseCase
import com.sss.monikaapps.feature.utils.device.domain.usecase.GetDeviceIdUseCase
import kotlinx.coroutines.launch


class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val getDeviceIdUseCase: GetDeviceIdUseCase,
    private val getAppVersionUseCase: GetAppVersionUseCase,
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _deviceId = MutableLiveData<String>()
    val deviceId: LiveData<String> = _deviceId

    init {
        fetchDeviceId()
    }

    private fun fetchDeviceId() {
        viewModelScope.launch {
            try {
                val id = getDeviceIdUseCase.execute()
                _deviceId.value = id
            } catch (e: Exception) {
                _deviceId.value = "Tidak tersedia"
            }
        }
    }

    fun login(employeeId: String) {
        viewModelScope.launch {
            _loginResult.value = Result.loading(null)
            try {
                val deviceId = getDeviceIdUseCase.execute()
                val appVersion = getAppVersionUseCase.execute()

                val request = LoginRequest(
                    employeeId = employeeId,
                    imei = deviceId,
                    versionApps = appVersion
                )

                val result = loginUseCase.execute(request)

                _loginResult.value = Result.success(result)

            } catch (e: Exception) {
                _loginResult.value = Result.error(null, e.message ?: "Login gagal")
            }
        }
    }
}


