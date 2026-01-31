package com.sss.monikaapps.feature.visit.data.remote

import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.feature.visit.domain.model.CheckInVisitRequest
import com.sss.monikaapps.feature.visit.domain.model.CheckOutVisitRequest
import com.sss.monikaapps.feature.visit.domain.model.toMultipartBody
import com.sss.monikaapps.feature.visit.domain.model.toMultipartImageParts
import com.sss.monikaapps.network.ApiService

class VisitRemoteDataSourceImpl(private val apiService: ApiService) :
    VisitRemoteDataSource {
    override suspend fun checkInVisit(
        payload: CheckInVisitRequest,
    ): String? {
        val response = apiService.checkInVisit(
            payload.toMultipartBody(),
            payload.toMultipartImageParts()
        )

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()?.data
    }

    override suspend fun checkOutVisit(trno: String, payload: CheckOutVisitRequest): String? {
        val response = apiService.checkOutVisit(
            trno,
            payload.toMultipartBody(),
            payload.toMultipartImageParts()
        )

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()?.data
    }

}