package com.sss.monikaapps.feature.expense.data.event

sealed class ExpenseUiEvent {
    data class Success(val message: String) : ExpenseUiEvent()
    data class Error(val message: String) : ExpenseUiEvent()
    data object NavigateBack : ExpenseUiEvent()
    data object RefreshDetail : ExpenseUiEvent()
}
