package com.sss.monikaapps.feature.version_check.data.remote

import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.feature.version_check.data.response.VersionResponse
import com.sss.monikaapps.network.ApiService

class VersionRemoteDataSourceImpl(private val apiService: ApiService) :
    VersionRemoteDataSource {
    override suspend fun fetchVersionAppsInServer(): VersionResponse? {
        val response = apiService.fetchAppVersion()

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

}