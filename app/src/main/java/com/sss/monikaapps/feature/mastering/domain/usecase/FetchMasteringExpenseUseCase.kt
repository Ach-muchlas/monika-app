package com.sss.monikaapps.feature.mastering.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.mastering.data.response.MasteringExpenseResponse
import com.sss.monikaapps.feature.mastering.domain.repository.MasteringRepository

class FetchMasteringExpenseUseCase(private val repository: MasteringRepository) {
    suspend operator fun invoke(): Result<MasteringExpenseResponse> {
        return try {
            val result = repository.fetchMasteringExpense()
            Result.success(result)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!!")
        }
    }
}