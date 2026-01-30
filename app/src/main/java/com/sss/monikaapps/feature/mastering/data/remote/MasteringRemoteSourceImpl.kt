package com.sss.monikaapps.feature.mastering.data.remote

import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.feature.mastering.data.response.MasteringExpenseResponse
import com.sss.monikaapps.network.ApiService

class MasteringRemoteSourceImpl(private val apiService: ApiService) : MasteringRemoteSource {
    override suspend fun fetchMasteringExpense(): MasteringExpenseResponse? {
        val response = apiService.fetchMasteringExpense()

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }
}