package com.sss.monikaapps.feature.login.data.remote

import com.sss.monikaapps.feature.login.data.model.LoginRequest
import com.sss.monikaapps.feature.login.data.response.LoginResponse

interface AuthRemoteDataSource {
    suspend fun userLogin(payload: LoginRequest) : LoginResponse
}
