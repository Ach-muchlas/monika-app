package com.sss.monikaapps.feature.expense.domain.usecase

import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.expense.domain.model.ExpenseDetailRequest
import com.sss.monikaapps.feature.expense.domain.model.ExpenseHeaderRequest
import com.sss.monikaapps.feature.expense.domain.repository.ExpensesRepository

class CreateExpenseDetailUseCase(private val repository: ExpensesRepository) {

    suspend operator fun invoke(
        trno: String,
        payload: ExpenseDetailRequest,
    ): Result<DefaultAddResponse> {
        return try {
            val createDetail = repository.createExpenseDetail(trno, payload)
            Result.success(createDetail)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!!")
        }
    }
}