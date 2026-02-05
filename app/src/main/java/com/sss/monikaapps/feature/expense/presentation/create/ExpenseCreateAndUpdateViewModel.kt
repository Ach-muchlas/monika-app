package com.sss.monikaapps.feature.expense.presentation.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.expense.domain.model.ExpenseDetailRequest
import com.sss.monikaapps.feature.expense.domain.model.ExpenseHeaderRequest
import com.sss.monikaapps.feature.expense.domain.model.ExpenseUpdateDetailRequest
import com.sss.monikaapps.feature.expense.domain.usecase.CreateExpenseDetailUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.CreateExpenseHeaderUseCase
import com.sss.monikaapps.feature.expense.domain.usecase.UpdateExpenseDetailUseCase
import com.sss.monikaapps.feature.mastering.data.response.DataItemMasteringExpense
import com.sss.monikaapps.feature.mastering.domain.usecase.FetchMasteringExpenseUseCase
import kotlinx.coroutines.launch

class ExpenseCreateAndUpdateViewModel(
    private val createExpenseHeaderUseCase: CreateExpenseHeaderUseCase,
    private val createExpenseDetailUseCase: CreateExpenseDetailUseCase,
    private val updateExpenseDetailUseCase: UpdateExpenseDetailUseCase,
    private val fetchMasteringExpenseUseCase: FetchMasteringExpenseUseCase,
) : ViewModel() {

    private val _createHeaderExpenseResult = MediatorLiveData<Result<DefaultAddResponse>>()
    val createHeaderExpenseResult: LiveData<Result<DefaultAddResponse>> = _createHeaderExpenseResult

    private val _createDetailExpenseResult = MediatorLiveData<Result<DefaultAddResponse>>()
    val createDetailExpenseResult: LiveData<Result<DefaultAddResponse>> = _createDetailExpenseResult

    private val _updateDetailExpenseResult = MediatorLiveData<Result<DefaultAddResponse>>()
    val updateDetailExpenseResult: LiveData<Result<DefaultAddResponse>> = _updateDetailExpenseResult

    private val _expenseCategories = MutableLiveData<List<DataItemMasteringExpense>>()
    val expenseCategories: LiveData<List<DataItemMasteringExpense>> = _expenseCategories

    private val _selectedExpenseCategory = MutableLiveData<DataItemMasteringExpense?>()
    val selectedExpenseCategory: LiveData<DataItemMasteringExpense?> = _selectedExpenseCategory

    fun createHeaderExpense(payload: ExpenseHeaderRequest) {
        viewModelScope.launch {
            _createHeaderExpenseResult.value = Result.loading(null)
            val result = createExpenseHeaderUseCase(payload)
            _createHeaderExpenseResult.value = result
        }
    }

    fun setSelectedCategoryById(categoryId: String) {
        val category = _expenseCategories.value
            ?.firstOrNull { it.id == categoryId }

        _selectedExpenseCategory.value = category
    }


    fun createExpenseDetail(trno: String, payload: ExpenseDetailRequest) {
        viewModelScope.launch {
            _createDetailExpenseResult.value = Result.loading(null)
            val result = createExpenseDetailUseCase(trno, payload)
            _createDetailExpenseResult.value = result
        }
    }

    fun updateExpenseDetail(trno: String, idDetail: String, payload: ExpenseUpdateDetailRequest) {
        Log.e("CHECK_", "Masuk sini")
        viewModelScope.launch {
            _updateDetailExpenseResult.value = Result.loading(null)
            val result = updateExpenseDetailUseCase(trno, idDetail, payload)
            _updateDetailExpenseResult.value = result
        }
    }


    fun fetchExpenseCategories() {
        viewModelScope.launch {
            val result = fetchMasteringExpenseUseCase()

            if (result.status == StatusNetwork.SUCCESS) {
                val list = result.data?.data
                    ?.filterNotNull()
                    ?.sortedBy { it.id }
                    ?: emptyList()

                _expenseCategories.value = list
            }
        }
    }

    fun selectExpenseCategory(item: DataItemMasteringExpense) {
        _selectedExpenseCategory.value = item
    }

    fun getSelectedExpenseCategoryId(): String? {
        return _selectedExpenseCategory.value?.id
    }

}