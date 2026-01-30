package com.sss.monikaapps.feature.expense.domain.usecase

import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.expense.domain.repository.ExpensesRepository

class DeleteExpenseDetailUseCase(private val repository: ExpensesRepository) {
    suspend operator fun invoke(trno: String, idDetail: String): Result<DefaultAddResponse> {
        return try {
            val createDetail = repository.deleteExpenseDetail(trno, idDetail)
            Result.success(createDetail)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!!")
        }
    }
}