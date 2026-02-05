package com.sss.monikaapps.feature.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.result.Result
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

    fun login(employeeId: String) {
        viewModelScope.launch {
            _loginResult.value = Result.loading(null)
            try {
                val deviceId = getDeviceIdUseCase()
                val appVersion = getAppVersionUseCase()

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


