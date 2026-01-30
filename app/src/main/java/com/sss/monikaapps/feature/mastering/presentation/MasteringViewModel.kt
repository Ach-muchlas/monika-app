package com.sss.monikaapps.feature.mastering.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.mastering.data.response.MasteringExpenseResponse
import com.sss.monikaapps.feature.mastering.domain.usecase.FetchMasteringExpenseUseCase
import kotlinx.coroutines.launch

class MasteringViewModel(private val expense: FetchMasteringExpenseUseCase) : ViewModel() {
    private val _masteringExpense = MutableLiveData<Result<MasteringExpenseResponse>>()
    val masteringExpense: LiveData<Result<MasteringExpenseResponse>> = _masteringExpense

    fun fetchMasterExpense() {
        viewModelScope.launch {
            _masteringExpense.value = Result.loading(null)

            val result = expense()

            _masteringExpense.value = result
        }
    }
}