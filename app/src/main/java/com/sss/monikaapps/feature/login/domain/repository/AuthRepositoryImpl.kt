package com.sss.monikaapps.feature.login.domain.repository

import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.feature.login.data.model.LoginRequest
import com.sss.monikaapps.feature.login.data.remote.AuthRemoteDataSource
import com.sss.monikaapps.feature.login.data.response.LoginResponse

class AuthRepositoryImpl(
    private val remote: AuthRemoteDataSource,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun userLogin(payload: LoginRequest): LoginResponse {
        val response = remote.userLogin(payload)

        sessionManager.saveDataUser(response.data)

        return response
    }
}
