package com.sss.monikaapps.feature.expense.domain.usecase

import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.expense.domain.repository.ExpensesRepository

class UnSubmitExpenseUseCase(private val repository: ExpensesRepository) {
    suspend operator fun invoke(trno: String): Result<DefaultAddResponse> {
        return try {
            val createDetail = repository.unSubmitExpense(trno)
            Result.success(createDetail)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!!")
        }
    }
}