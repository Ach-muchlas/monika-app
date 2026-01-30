package com.sss.monikaapps.feature.expense.presentation.detail

sealed class ExpenseDetailDialogAction {
    data class DeleteHeader(val trno: String) : ExpenseDetailDialogAction()
    data class DeleteDetail(val trno: String, val idDetail: String) : ExpenseDetailDialogAction()
    data class EditDetail(val trno: String, val idDetail: String, val netAmount : String,val  note : String) : ExpenseDetailDialogAction()
}