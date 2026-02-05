package com.sss.monikaapps.feature.expense.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.expense.data.response.DetailExpanseResponse
import com.sss.monikaapps.feature.expense.domain.usecase.DeleteExpenseDetailUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.DeleteExpenseHeaderUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.FetchDetailExpenseUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.SubmitExpenseUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.UnSubmitExpenseUseCase
import kotlinx.coroutines.launch

class ExpenseDetailViewModel(
    private val fetchDetailExpanseUseCase: FetchDetailExpenseUseCase,
    private val submitExpenseUseCase: SubmitExpenseUseCase,
    private val unSubmitExpenseUseCase: UnSubmitExpenseUseCase,
    private val deleteExpenseHeaderUseCase: DeleteExpenseHeaderUseCase,
    private val deleteExpenseDetailUseCase: DeleteExpenseDetailUseCase,
) : ViewModel() {

    private val _detailExpanseResult = MutableLiveData<Result<DetailExpanseResponse>>()
    val detailExpanseResult: LiveData<Result<DetailExpanseResponse>> = _detailExpanseResult

    private val _submitExpenseResult = MediatorLiveData<Result<DefaultAddResponse>>()
    val submitExpenseResult: LiveData<Result<DefaultAddResponse>> = _submitExpenseResult

    private val _unSubmitExpenseResult = MediatorLiveData<Result<DefaultAddResponse>>()
    val unSubmitExpenseResult: LiveData<Result<DefaultAddResponse>> = _unSubmitExpenseResult

    private val _deleteHeaderExpenseResult = MediatorLiveData<Result<DefaultAddResponse>>()
    val deleteHeaderExpenseResult: LiveData<Result<DefaultAddResponse>> = _deleteHeaderExpenseResult

    private val _deleteDetailExpenseResult = MediatorLiveData<Result<DefaultAddResponse>>()
    val deleteDetailExpenseResult: LiveData<Result<DefaultAddResponse>> = _deleteDetailExpenseResult

    fun fetchDetailExpanse(trno: String) = viewModelScope.launch {
        _detailExpanseResult.value = Result.loading(null)

        try {
            val result = fetchDetailExpanseUseCase.invoke(trno)

            if (result == null) {
                _detailExpanseResult.value = Result.error(null, "Data kosong")
            } else {
                _detailExpanseResult.value = Result.success(result)
            }

        } catch (e: Exception) {
            _detailExpanseResult.value =
                Result.error(null, e.message ?: "Gagal mengambil detail expanse")
        }
    }

    fun submitExpense(trno: String) {
        viewModelScope.launch {
            _submitExpenseResult.value = Result.loading(null)
            val result = submitExpenseUseCase(trno)
            _submitExpenseResult.value = result
        }
    }

    fun unSubmitExpense(trno: String) {
        viewModelScope.launch {
            _submitExpenseResult.value = Result.loading(null)
            val result = unSubmitExpenseUseCase(trno)
            _submitExpenseResult.value = result
        }
    }

    fun deleteExpenseHeader(trno: String) {
        viewModelScope.launch {
            _deleteHeaderExpenseResult.value = Result.loading(null)
            val result = deleteExpenseHeaderUseCase(trno)
            _deleteHeaderExpenseResult.value = result
        }
    }

    fun deleteExpenseDetail(trno: String, idDetail: String) {
        viewModelScope.launch {
            _deleteDetailExpenseResult.value = Result.loading(null)
            val result = deleteExpenseDetailUseCase(trno, idDetail)
            _deleteDetailExpenseResult.value = result
        }
    }
}