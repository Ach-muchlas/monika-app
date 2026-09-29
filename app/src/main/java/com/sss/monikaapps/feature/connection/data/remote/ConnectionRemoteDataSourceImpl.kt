package com.sss.monikaapps.feature.connection.data.remote

import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.feature.connection.data.response.CheckConnectionResponse
import com.sss.monikaapps.network.ApiService

class ConnectionRemoteDataSourceImpl(private val apiService: ApiService) :
    ConnectionRemoteDataSource {
    override suspend fun fetchCheckConnectionDatabase(): CheckConnectionResponse? {
        val response = apiService.fetchCheckConnection()

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

}