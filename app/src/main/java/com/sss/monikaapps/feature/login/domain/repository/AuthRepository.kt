package com.sss.monikaapps.feature.login.domain.repository

import androidx.lifecycle.LiveData
import com.sss.monikaapps.feature.login.data.model.LoginRequest
import com.sss.monikaapps.feature.login.data.response.LoginResponse
import com.sss.monikaapps.common.result.Result

interface AuthRepository {
     suspend fun userLogin(payload: LoginRequest): LoginResponse
}