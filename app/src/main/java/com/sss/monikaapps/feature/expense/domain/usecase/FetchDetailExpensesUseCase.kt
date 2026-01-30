package com.sss.monikaapps.feature.expense.domain.usecase

import com.sss.monikaapps.feature.expense.data.response.DetailExpanseResponse
import com.sss.monikaapps.feature.expense.domain.repository.ExpensesRepository

class FetchDetailExpenseUseCase(
    private val repository: ExpensesRepository
) {
    suspend operator fun invoke(trno: String): DetailExpanseResponse? {
        return repository.fetchExpenseDetail(trno)
    }
}
