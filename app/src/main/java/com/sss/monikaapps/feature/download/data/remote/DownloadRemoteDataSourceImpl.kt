package com.sss.monikaapps.feature.download.data.remote

import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse
import com.sss.monikaapps.network.ApiService

class DownloadRemoteDataSourceImpl(private val apiService: ApiService) : DownloadRemoteDataSource {
    override suspend fun fetchDownloadVisit(): VisitDownloadResponse? {
        val response = apiService.fetchDownloadVisit()

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }
}