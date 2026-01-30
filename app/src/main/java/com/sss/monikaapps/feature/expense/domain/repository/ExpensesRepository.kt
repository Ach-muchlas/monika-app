package com.sss.monikaapps.feature.expense.domain.repository

import androidx.paging.PagingData
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.expense.data.response.DataItemExpenses
import com.sss.monikaapps.feature.expense.data.response.DetailExpanseResponse
import com.sss.monikaapps.feature.expense.domain.model.ExpenseDetailRequest
import com.sss.monikaapps.feature.expense.domain.model.ExpenseHeaderRequest
import com.sss.monikaapps.feature.expense.domain.model.ExpenseUpdateDetailRequest
import kotlinx.coroutines.flow.Flow

interface ExpensesRepository {
    fun fetchDataExpenses(status: Int): Flow<PagingData<DataItemExpenses>>
    suspend fun fetchExpenseDetail(trno: String): DetailExpanseResponse?
    suspend fun createExpenseHeader(payload: ExpenseHeaderRequest): DefaultAddResponse?
    suspend fun createExpenseDetail(
        trno: String,
        payload: ExpenseDetailRequest,
    ): DefaultAddResponse?

    suspend fun updateExpenseDetail(
        trno: String,
        idDetail: String,
        payload: ExpenseUpdateDetailRequest,
    ): DefaultAddResponse?

    suspend fun deleteExpenseHeader(trno: String): DefaultAddResponse?
    suspend fun deleteExpenseDetail(trno: String, idDetail: String): DefaultAddResponse?
}