package com.sss.monikaapps.feature.expense.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.network.ApiService
import com.sss.monikaapps.feature.expense.data.response.DataItemExpenses

class ExpensesPagingSource(private val apiService: ApiService, private val status: Int) :
    PagingSource<Int, DataItemExpenses>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
        private const val PAGE_SIZE = 10
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItemExpenses> {
        return try {
            val pageIndex = params.key ?: STARTING_PAGE_INDEX

            val response =
                apiService.fetchDataExpanses(status, pageIndex, PAGE_SIZE)

            if (response.isSuccessful) {
                val data = response.body()?.data ?: emptyList()
                LoadResult.Page(
                    data = data,
                    prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                    nextKey = if (data.isEmpty()) null else pageIndex + 1
                )
            } else {
                val errorMsg = parseErrorResponse(response)
                LoadResult.Error(Exception(errorMsg))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataItemExpenses>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}