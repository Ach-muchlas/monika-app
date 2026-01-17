package com.sss.monikaapps.feature.login.data.repository

import androidx.lifecycle.LiveData
import com.sss.monikaapps.feature.login.data.model.LoginRequest
import com.sss.monikaapps.feature.login.data.response.LoginResponse
import com.sss.monikaapps.common.result.Result

interface AuthRepository {
     fun userLogin(payload: LoginRequest): LiveData<Result<LoginResponse>>
}