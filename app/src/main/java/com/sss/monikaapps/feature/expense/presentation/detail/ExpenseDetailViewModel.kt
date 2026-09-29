package com.sss.monikaapps.feature.expense.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.expense.data.event.ExpenseUiEvent
import com.sss.monikaapps.feature.expense.data.response.DetailExpanseResponse
import com.sss.monikaapps.feature.expense.domain.usecase.DeleteExpenseDetailUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.DeleteExpenseHeaderUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.FetchDetailExpenseUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.SubmitExpenseUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.UnSubmitExpenseUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _uiEvent = MutableSharedFlow<ExpenseUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun fetchDetailExpanse(trno: String) = viewModelScope.launch {
        _detailExpanseResult.value = Result.loading(null)
        try {
            val result = fetchDetailExpanseUseCase(trno)
            _detailExpanseResult.value =
                result?.let { Result.success(it) }
                    ?: Result.error(null, "Data kosong")
        } catch (e: Exception) {
            _detailExpanseResult.value =
                Result.error(null, e.message ?: "Gagal mengambil detail")
        }
    }

    fun submitExpense(trno: String) = viewModelScope.launch {
        val result = submitExpenseUseCase(trno)
        handleResult(result)
    }

    fun unSubmitExpense(trno: String) = viewModelScope.launch {
        val result = unSubmitExpenseUseCase(trno)
        handleResult(result)
    }

    fun deleteExpenseHeader(trno: String) = viewModelScope.launch {
        val result = deleteExpenseHeaderUseCase(trno)
        handleResult(result, navigateBack = true)
    }

    fun deleteExpenseDetail(trno: String, idDetail: String) = viewModelScope.launch {
        val result = deleteExpenseDetailUseCase(trno, idDetail)
        handleResult(result)
    }

    private suspend fun handleResult(
        result: Result<DefaultAddResponse>,
        navigateBack: Boolean = false,
    ) {
        when (result.status) {
            StatusNetwork.SUCCESS -> {
                _uiEvent.emit(
                    ExpenseUiEvent.Success(result.data?.message.orEmpty())
                )
                if (navigateBack) {
                    _uiEvent.emit(ExpenseUiEvent.NavigateBack)
                } else {
                    _uiEvent.emit(ExpenseUiEvent.RefreshDetail)
                }
            }

            StatusNetwork.ERROR -> {
                _uiEvent.emit(
                    ExpenseUiEvent.Error(result.message ?: "Terjadi kesalahan")
                )
            }

            else -> Unit
        }
    }
}
