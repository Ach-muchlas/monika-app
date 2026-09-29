package com.sss.monikaapps.feature.expense.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sss.monikaapps.feature.expense.data.response.DataItemExpenses
import com.sss.monikaapps.feature.expense.domain.usecase.FetchExpensesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

class ExpensesViewModel(
    private val fetchExpansesUseCase: FetchExpensesUseCase,
) : ViewModel() {

    private val _refreshTrigger = MutableStateFlow(0)

    private val _status = MutableStateFlow(7)
    val status: StateFlow<Int> = _status

    @OptIn(ExperimentalCoroutinesApi::class)
    val expansesResult: Flow<PagingData<DataItemExpenses>> =
        combine(_status, _refreshTrigger) { status, _ ->
            status
        }.flatMapLatest { status ->
            fetchExpansesUseCase(status)
        }.cachedIn(viewModelScope)

    fun setStatus(status: Int) {
        _status.value = status
        refresh()
    }


    fun refresh() {
        _refreshTrigger.update { it + 1 }
    }
}
