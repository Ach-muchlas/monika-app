package com.sss.monikaapps.feature.expense.domain.repository

import androidx.paging.PagingData
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.expense.data.remote.ExpensesRemoteDataSource
import com.sss.monikaapps.feature.expense.data.response.DataItemExpenses
import com.sss.monikaapps.feature.expense.data.response.DetailExpanseResponse
import com.sss.monikaapps.feature.expense.domain.model.ExpenseDetailRequest
import com.sss.monikaapps.feature.expense.domain.model.ExpenseHeaderRequest
import com.sss.monikaapps.feature.expense.domain.model.ExpenseUpdateDetailRequest
import kotlinx.coroutines.flow.Flow

class ExpensesRepositoryImpl(private val remote: ExpensesRemoteDataSource) : ExpensesRepository {
    override fun fetchDataExpenses(status: Int): Flow<PagingData<DataItemExpenses>> {
        return remote.fetchDataExpanses(status)
    }

    override suspend fun fetchExpenseDetail(trno: String): DetailExpanseResponse? {
        return remote.fetchDetailExpanse(trno)
    }

    override suspend fun createExpenseHeader(payload: ExpenseHeaderRequest): DefaultAddResponse? {
        return remote.createHeaderExpense(payload)
    }

    override suspend fun createExpenseDetail(
        trno: String,
        payload: ExpenseDetailRequest,
    ): DefaultAddResponse? {
        return remote.createExpenseDetail(trno, payload)
    }

    override suspend fun updateExpenseDetail(
        trno: String,
        idDetail: String,
        payload: ExpenseUpdateDetailRequest,
    ): DefaultAddResponse? {
        return remote.updateExpenseDetail(trno, idDetail, payload)
    }

    override suspend fun submitExpense(trno: String): DefaultAddResponse? {
        return remote.submitExpense(trno)
    }

    override suspend fun unSubmitExpense(trno: String): DefaultAddResponse? {
        return remote.unSubmitExpense(trno)
    }

    override suspend fun deleteExpenseHeader(trno: String): DefaultAddResponse? {
        return remote.deleteExpenseHeader(trno)
    }

    override suspend fun deleteExpenseDetail(trno: String, idDetail: String): DefaultAddResponse? {
        return remote.deleteExpenseDetail(trno, idDetail)
    }
}