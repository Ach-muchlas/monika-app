package com.sss.monikaapps.feature.activity.domain.repository

import androidx.lifecycle.LiveData
import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.data.response.DataItemActivities
import com.sss.monikaapps.feature.activity.data.response.DataItemDetailActivity
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckInRequest
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckOutRequest
import kotlinx.coroutines.flow.Flow

interface ActivitiesRepository {
    suspend fun insertActivity(payload: ActivityEntity)
    suspend fun updateCheckOut(idMobile: String, timeEnd: String, lat: String, lon: String)

    suspend fun fetchPhotos(parentId: String): List<PhotoEntity>
    suspend fun fetchPhotosByFeatureId(parentId: String, feature :String): List<PhotoEntity>
    suspend fun fetchDetailActivity(id: String): ActivityEntity

    suspend fun markCheckOutIsDone(localId: String)
    suspend fun markCheckInSynced(localId: String, serverId: String)
    suspend fun markCheckOutSynced(localId: String)

    suspend fun checkInRemote(request: ActivityCheckInRequest): String?
    suspend fun checkOutRemote(trno: String, request: ActivityCheckOutRequest): String?

    suspend fun fetchActivities(): List<DataItemActivities>
    suspend fun fetchDetailLocal(trno: String): DataItemDetailActivity
    suspend fun fetchDetailRemote(trno: String): DataItemDetailActivity?

    suspend fun insertLogActivities(title: String, desc: String)
    suspend fun fetchActivitiesLocal(): List<ActivityEntity>

    fun countDataCheckoutNotSync () : Flow<Int>
}