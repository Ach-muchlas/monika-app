package com.sss.monikaapps.feature.expanse.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.activity.data.paging.ExpansesPagingSource
import com.sss.monikaapps.feature.expanse.data.response.DataItemExpanses
import com.sss.monikaapps.feature.expanse.data.response.DetailExpanseResponse
import com.sss.monikaapps.network.ApiService
import kotlinx.coroutines.flow.Flow

class ExpansesRepositoryImpl(private val apiService: ApiService) : ExpansesRepository {
    override fun fetchDataExpanses(status: Int): Flow<PagingData<DataItemExpanses>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                ExpansesPagingSource(apiService, status)
            }
        ).flow
    }

    override fun fetchDetailExpanse(trno: String): LiveData<Result<DetailExpanseResponse>> =
        liveData {
            emit(Result.loading(null))
            try {
                val response = apiService.fetchDetailExpanse(trno)

                if (response.isSuccessful) {
                    emit(Result.success(response.body()))
                } else {
                    emit(Result.error(null, parseErrorResponse(response)))
                }
            } catch (e: Exception) {
                emit(Result.error(null, e.message ?: "ERROR OCCURRED"))
            }
        }
}