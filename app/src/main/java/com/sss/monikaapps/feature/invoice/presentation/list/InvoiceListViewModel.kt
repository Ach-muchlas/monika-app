package com.sss.monikaapps.feature.invoice.presentation.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.domain.usecase.GetListCustomerInvoiceUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.SyncManualInvoiceUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InvoiceListViewModel(
    listCustomerInvoiceUseCase: GetListCustomerInvoiceUseCase,
    private val syncManualInvoiceUseCase: SyncManualInvoiceUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _selectedStatus = MutableStateFlow(7)
    val selectedStatus = _selectedStatus.asStateFlow()

    private val _syncManualResult = MediatorLiveData<Result<String>?>()
    val syncManualResult: LiveData<Result<String>?> = _syncManualResult

    @OptIn(ExperimentalCoroutinesApi::class)
    val customerInvoice: StateFlow<Result<List<CustomerInvoiceEntity>>> =
        combine(_searchQuery, _selectedStatus) { query, status ->
            Pair(query, status)
        }.flatMapLatest { (query, status) ->
            listCustomerInvoiceUseCase(query, status)
        }.map { Result.success(it) }.onStart { emit(Result.loading(null)) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.loading(null)
        )

    fun onSearchQueryChanged(query: String) {
        Log.e("CHECK_QUERY", "query : $query")
        _searchQuery.value = query
    }

    fun onStatusChanged(status: Int) {
        _selectedStatus.value = status
    }


    fun syncManual() {
        viewModelScope.launch {
            _syncManualResult.value = Result.loading(null)
            val result = syncManualInvoiceUseCase()
            _syncManualResult.value = result
        }
    }

    fun clearSyncState() {
        _syncManualResult.value = null
    }
}