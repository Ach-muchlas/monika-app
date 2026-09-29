package com.sss.monikaapps.feature.login.domain.usecase

import com.sss.monikaapps.feature.login.data.model.LoginRequest
import com.sss.monikaapps.feature.login.data.response.LoginResponse
import com.sss.monikaapps.feature.login.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend fun execute(payload: LoginRequest): LoginResponse {
        if (payload.employeeId.isBlank()) {
            throw IllegalArgumentException("Employee ID tidak boleh kosong")
        }

        return repository.userLogin(payload)
    }
}
