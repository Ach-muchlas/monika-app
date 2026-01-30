package com.sss.monikaapps.feature.login.data.remote

import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.feature.login.data.model.LoginRequest
import com.sss.monikaapps.feature.login.data.response.LoginResponse
import com.sss.monikaapps.network.ApiService

class AuthRemoteDataSourceImpl(private val apiService: ApiService) : AuthRemoteDataSource {
    override suspend fun userLogin(payload: LoginRequest): LoginResponse{
        val response = apiService.loginUser(payload.employeeId, payload.imei, payload.versionApps)

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body() ?: throw RuntimeException("Response body null")
    }
}
