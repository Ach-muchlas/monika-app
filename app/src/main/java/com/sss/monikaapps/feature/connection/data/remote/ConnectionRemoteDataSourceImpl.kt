package com.sss.monikaapps.feature.connection.data.remote

import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.feature.connection.data.response.VersionResponse
import com.sss.monikaapps.network.ApiService

class ConnectionRemoteDataSourceImpl(private val apiService: ApiService) :
    ConnectionRemoteDataSource {
    override suspend fun fetchVersionAppsInServer(): VersionResponse? {
        val response = apiService.fetchAppVersion()

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

}