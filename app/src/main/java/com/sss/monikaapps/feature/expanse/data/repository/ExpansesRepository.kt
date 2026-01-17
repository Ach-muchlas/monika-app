package com.sss.monikaapps.feature.expanse.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.sss.monikaapps.feature.expanse.data.response.DataItemExpanses
import com.sss.monikaapps.feature.expanse.data.response.DetailExpanseResponse
import kotlinx.coroutines.flow.Flow
import com.sss.monikaapps.common.result.Result

interface ExpansesRepository {
    fun fetchDataExpanses(status: Int): Flow<PagingData<DataItemExpanses>>
    fun fetchDetailExpanse(trno: String): LiveData<Result<DetailExpanseResponse>>
}