package com.sss.monikaapps.feature.activity.data.remote

import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckInRequest
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckOutRequest
import com.sss.monikaapps.feature.activity.data.response.DataItemActivities
import com.sss.monikaapps.feature.activity.data.response.DataItemDetailActivity

interface ActivityRemoteDataSource {
    suspend fun checkIn(request: ActivityCheckInRequest): String?
    suspend fun checkOut(trno: String, request: ActivityCheckOutRequest): String?
    suspend fun fetchActivities(date: String): List<DataItemActivities>
    suspend fun fetchDetail(trno: String): DataItemDetailActivity?
}
