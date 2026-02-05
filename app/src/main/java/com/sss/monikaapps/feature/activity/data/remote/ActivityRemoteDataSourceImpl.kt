package com.sss.monikaapps.feature.activity.data.remote

import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.feature.activity.data.response.DataItemActivities
import com.sss.monikaapps.feature.activity.data.response.DataItemDetailActivity
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckInRequest
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckOutRequest
import com.sss.monikaapps.feature.activity.domain.model.toMultipartBody
import com.sss.monikaapps.feature.activity.domain.model.toMultipartImageParts
import com.sss.monikaapps.network.ApiService
import com.sss.monikaapps.network.interceptor.ConnectionLostException
import java.io.IOException

class ActivityRemoteDataSourceImpl(
    private val apiService: ApiService,
) : ActivityRemoteDataSource {

    override suspend fun checkIn(request: ActivityCheckInRequest): String? {
        try {
            val response = apiService.checkInActivity(
                request.toMultipartBody(),
                request.toMultipartImageParts()
            )

            if (!response.isSuccessful) {
                throw ConnectionLostException(parseErrorResponse(response))
            }

            return response.body()?.data

        } catch (e: IOException) {
            throw e
        }
    }


    override suspend fun checkOut(
        trno: String,
        request: ActivityCheckOutRequest,
    ): String? {
        try {
            val response = apiService.checkOutActivity(
                trno,
                request.toMultipartBody(),
                request.toMultipartImageParts()
            )

            if (!response.isSuccessful) {
                throw ConnectionLostException(parseErrorResponse(response))
            }

            return response.body()?.data
        } catch (e: IOException) {
            throw e
        }
    }

    override suspend fun fetchActivities(date: String): List<DataItemActivities> {
        val response = apiService.fetchDataActivities(date)

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()?.data.orEmpty()
    }

    override suspend fun fetchDetail(trno: String): DataItemDetailActivity? {
        val response = apiService.fetchDetailActivity(trno)

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()?.data
    }
}
