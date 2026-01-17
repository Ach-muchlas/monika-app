package com.sss.monikaapps.feature.login.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.network.ApiService
import com.sss.monikaapps.feature.login.data.model.LoginRequest
import com.sss.monikaapps.feature.login.data.response.LoginResponse
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.common.result.Result

class AuthRepositoryImpl(private val apiService: ApiService) : AuthRepository {
    override fun userLogin(payload: LoginRequest): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.loading(null))
        try {
            val response = apiService.loginUser(payload.employeeId, payload.imei, "1.0.0")
            if (response.isSuccessful) {
                val session = SessionManager.getInstance()
                session.saveDataUser(response.body()?.data)
                emit(Result.success(response.body()))
            } else {
                emit(Result.error(null, parseErrorResponse(response)))
            }
        } catch (e: Exception) {
            emit(Result.error(null, e.message ?: "ERROR OCCURRED"))
        }
    }

}