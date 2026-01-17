package com.sss.monikaapps.feature.expanse.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sss.monikaapps.feature.expanse.data.repository.ExpansesRepository
import com.sss.monikaapps.feature.expanse.data.response.DataItemExpanses
import com.sss.monikaapps.feature.expanse.data.response.DetailExpanseResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import com.sss.monikaapps.common.result.Result

class ExpansesViewModel(private val repository: ExpansesRepository) : ViewModel() {

    private val _status = MutableStateFlow(7)
    val status: StateFlow<Int> = _status

    private val _detailExpanseResult = MutableLiveData<Result<DetailExpanseResponse>>()
    val detailExpanseResult: LiveData<Result<DetailExpanseResponse>> = _detailExpanseResult

    @OptIn(ExperimentalCoroutinesApi::class)
    val expansesResult: Flow<PagingData<DataItemExpanses>> =
        status.flatMapLatest { status ->
            repository.fetchDataExpanses(status)
        }
            .cachedIn(viewModelScope)

    fun setStatus(status: Int) {
        _status.value = status
    }

    fun fetchDetailExpanse(trno: String) {
        repository.fetchDetailExpanse(trno).observeForever { _detailExpanseResult.value = it }

    }
}