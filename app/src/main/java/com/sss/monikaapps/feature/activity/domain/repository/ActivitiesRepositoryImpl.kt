package com.sss.monikaapps.feature.activity.domain.repository

import android.util.Log
import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDate
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.common.mapper.MapperActivity
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.data.local.ActivityLocalDataSource
import com.sss.monikaapps.feature.activity.data.remote.ActivityRemoteDataSource
import com.sss.monikaapps.feature.activity.data.response.DataItemActivities
import com.sss.monikaapps.feature.activity.data.response.DataItemDetailActivity
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckInRequest
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckOutRequest
import com.sss.monikaapps.feature.activity.utils.NetworkChecker

class ActivitiesRepositoryImpl(
    private val local: ActivityLocalDataSource,
    private val remote: ActivityRemoteDataSource,
    private val networkChecker: NetworkChecker,
    private val sessionManager: SessionManager,
) : ActivitiesRepository {

    override suspend fun insertActivity(payload: ActivityEntity) {
        local.insertActivity(payload)
        local.markCheckInDone(payload.id)
    }

    override suspend fun updateCheckOut(
        idMobile: String,
        timeEnd: String,
        lat: String,
        lon: String,
    ) {
        local.updateCheckOut(idMobile, timeEnd, lat, lon)
        local.markCheckOutDone(idMobile)
    }

    override suspend fun fetchPhotos(parentId: String): List<PhotoEntity> {
        return local.fetchPhotos(parentId)
    }

    override suspend fun fetchPhotosByFeatureId(
        parentId: String,
        feature: String,
    ): List<PhotoEntity> {
        return local.fetchPhotosByParentIdAndParentType(id = parentId, feature)
    }

    override suspend fun fetchDetailActivity(id: String): ActivityEntity {
        return local.fetchDetailActivity(id)
    }

    override suspend fun markCheckOutIsDone(localId: String) {
        return local.markCheckOutDone(localId)
    }

    override suspend fun markCheckInSynced(localId: String, serverId: String) {
        local.markCheckInSynced(localId, serverId)
    }

    override suspend fun markCheckOutSynced(localId: String) {
        local.markCheckOutSynced(localId)
    }

    override suspend fun checkInRemote(request: ActivityCheckInRequest): String? {
        return remote.checkIn(request)
    }

    override suspend fun checkOutRemote(
        trno: String,
        request: ActivityCheckOutRequest,
    ): String? {
        return remote.checkOut(trno, request)
    }

    override suspend fun fetchActivities(): List<DataItemActivities> {
        val localData = MapperActivity.mapperActivities(local.fetchActivities())

        Log.e("NETWORK", "isConnected = ${networkChecker.isConnected()}")
        Log.e("DATA", "data = $localData")

        if (!networkChecker.isConnected()) return localData

        val serverData = remote.fetchActivities(getCurrentDate())
            .map { it.copy(locationData = 1) }

        return (localData + serverData).distinctBy { it.trnoMobile }
    }

    override suspend fun fetchDetailLocal(
        trno: String,
    ): DataItemDetailActivity {

        val headerEntity = local.fetchDetailActivity(trno)
        val user = sessionManager.getDataUser()
        val photos = local.fetchPhotos(trno)

        val photoResponse =
            MapperActivity.mapperPhotoEntityToPhotoResponse(photos)

        return DataItemDetailActivity(
            header = MapperActivity.mapperDetailHeaderActivities(
                headerEntity,
                user.employeeName.toString()
            ),
            fotoActivity = photoResponse
        )
    }

    override suspend fun fetchDetailRemote(
        trno: String,
    ): DataItemDetailActivity? {
        return remote.fetchDetail(trno)
    }

    override suspend fun insertLogActivities(title: String, desc: String) =
        local.insertLog(title, desc)

    override suspend fun fetchActivitiesLocal(): List<ActivityEntity> = local.fetchActivities()

}