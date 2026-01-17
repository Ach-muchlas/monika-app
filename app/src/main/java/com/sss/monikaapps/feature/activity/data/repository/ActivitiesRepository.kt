package com.sss.monikaapps.feature.activity.data.repository

import androidx.lifecycle.LiveData
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.data.response.DataItemActivities
import com.sss.monikaapps.feature.activity.data.response.DataItemDetailActivity
import com.sss.monikaapps.common.result.Result

interface ActivitiesRepository {
    fun fetchDataActivities(): LiveData<Result<List<DataItemActivities>>>
    fun fetchDetailActivity(trno: String): LiveData<Result<DataItemDetailActivity>>
    fun fetchDetailActivityLocalDatabase(
        trno: String,
        employeeId: String,
    ): LiveData<Result<DataItemDetailActivity>>

    fun createActivity(payload: ActivityEntity): LiveData<Result<String>>

    fun checkOutActivity(
        trno: String,
        idMobile: String,
        endTime: String,
        latitude: String,
        longitude: String,
    ): LiveData<Result<String>>

    fun syncManualActivities(): LiveData<Result<String>>

}