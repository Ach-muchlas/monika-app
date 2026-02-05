package com.sss.monikaapps.feature.expense.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.expense.data.paging.ExpensesPagingSource
import com.sss.monikaapps.feature.expense.data.response.DataItemExpenses
import com.sss.monikaapps.feature.expense.data.response.DetailExpanseResponse
import com.sss.monikaapps.feature.expense.domain.model.ExpenseDetailRequest
import com.sss.monikaapps.feature.expense.domain.model.ExpenseHeaderRequest
import com.sss.monikaapps.feature.expense.domain.model.ExpenseUpdateDetailRequest
import com.sss.monikaapps.feature.expense.domain.model.toMultipartBody
import com.sss.monikaapps.feature.expense.domain.model.toMultipartImageParts
import com.sss.monikaapps.network.ApiService
import kotlinx.coroutines.flow.Flow

class ExpensesRemoteDataSourceImpl(private val apiService: ApiService) : ExpensesRemoteDataSource {
    override fun fetchDataExpanses(status: Int): Flow<PagingData<DataItemExpenses>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                ExpensesPagingSource(apiService, status)
            }).flow
    }

    override suspend fun fetchDetailExpanse(trno: String): DetailExpanseResponse? {
        val response = apiService.fetchDetailExpanse(trno)

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun createHeaderExpense(payload: ExpenseHeaderRequest): DefaultAddResponse? {
        val response = apiService.createExpenseHeader(date = payload.date, note = payload.note)

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun createExpenseDetail(
        trno: String,
        payload: ExpenseDetailRequest,
    ): DefaultAddResponse? {
        val response = apiService.createExpenseDetail(
            trno = trno,
            params = payload.toMultipartBody(),
            photos = payload.toMultipartImageParts()
        )

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun updateExpenseDetail(
        trno: String,
        idDetail: String,
        payload: ExpenseUpdateDetailRequest,
    ): DefaultAddResponse? {
        val response = apiService.updateExpenseDetail(
            trno = trno,
            idExpense = idDetail,
            params = payload.toMultipartBody(),
            photos = payload.toMultipartImageParts()
        )

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun submitExpense(trno: String): DefaultAddResponse? {
        val response = apiService.submitExpense(trno)

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun unSubmitExpense(trno: String): DefaultAddResponse? {
        val response = apiService.unSubmitExpense(trno)

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun deleteExpenseHeader(trno: String): DefaultAddResponse? {
        val response = apiService.deleteExpenseHeader(trno)

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun deleteExpenseDetail(trno: String, idDetail: String): DefaultAddResponse? {
        val response = apiService.deleteExpenseDetail(trno, idDetail)

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }


}