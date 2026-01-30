package com.sss.monikaapps.feature.expense.domain.usecase

import androidx.paging.PagingData
import com.sss.monikaapps.feature.expense.data.response.DataItemExpenses
import com.sss.monikaapps.feature.expense.domain.repository.ExpensesRepository
import kotlinx.coroutines.flow.Flow

class FetchExpensesUseCase(
    private val repository: ExpensesRepository
) {
    operator fun invoke(status: Int): Flow<PagingData<DataItemExpenses>> {
        return repository.fetchDataExpenses(status)
    }
}
