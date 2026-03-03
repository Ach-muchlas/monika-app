package com.sss.monikaapps.feature.invoice.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.domain.usecase.GetListCustomerInvoiceUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.SearchListCustomerInvoiceUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class InvoiceViewModel(
    listCustomerInvoiceUseCase: GetListCustomerInvoiceUseCase,
    searchCustomerInvoiceUseCase: SearchListCustomerInvoiceUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val customerInvoice: StateFlow<Result<List<CustomerInvoiceEntity>>> =
        _searchQuery.flatMapLatest { query ->
            if (query.isEmpty()) {
                listCustomerInvoiceUseCase()
            } else {
                searchCustomerInvoiceUseCase(query)
            }
        }
            .map { Result.success(it) }
            .onStart { emit(Result.loading(null)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Result.loading(null)
            )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}