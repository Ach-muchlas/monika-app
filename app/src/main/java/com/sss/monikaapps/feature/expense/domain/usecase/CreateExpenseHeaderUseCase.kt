package com.sss.monikaapps.feature.expense.domain.usecase

import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.expense.domain.model.ExpenseDetailRequest
import com.sss.monikaapps.feature.expense.domain.model.ExpenseHeaderRequest
import com.sss.monikaapps.feature.expense.domain.repository.ExpensesRepository

class CreateExpenseHeaderUseCase(private val repository: ExpensesRepository) {
    suspend operator fun invoke(payload: ExpenseHeaderRequest): Result<DefaultAddResponse> {
        return try {
            val create = repository.createExpenseHeader(payload)
            Result.success(create)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!!")
        }
    }
}