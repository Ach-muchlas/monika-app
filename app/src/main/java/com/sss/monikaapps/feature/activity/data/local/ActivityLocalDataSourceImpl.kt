package com.sss.monikaapps.feature.activity.data.local

import com.sss.monikaapps.common.db.dao.PhotoDao
import com.sss.monikaapps.common.db.entity.LogEntity
import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDate
import com.sss.monikaapps.feature.activity.data.dao.ActivityDao
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity

class ActivityLocalDataSourceImpl(
    private val activityDao: ActivityDao,
    private val photoDao: PhotoDao,
) : ActivityLocalDataSource {

    override suspend fun fetchActivities(): List<ActivityEntity> {
        return activityDao.fetchActivity(getCurrentDate())
    }

    override suspend fun fetchActivitiesLocalDatabase(): List<ActivityEntity> {
        return activityDao.fetchDataActivityLocalDatabase()
    }

    override suspend fun insertActivity(entity: ActivityEntity) {
        activityDao.insertActivity(entity)
    }

    override suspend fun fetchDetailActivity(id: String): ActivityEntity {
        return activityDao.fetchDataDetailActivityLocalDatabase(id)
    }

    override suspend fun fetchPhotos(id: String): List<PhotoEntity> {
        return photoDao.fetchPhotosByParentId(id)
    }

    override suspend fun fetchPhotosByParentIdAndParentType(
        id: String,
        type: String,
    ): List<PhotoEntity> {
        return photoDao.fetchPhotoByParentIdAndParentType(id, type)
    }

    override suspend fun markCheckInDone(id: String) {
        activityDao.markCheckInIsDoneInLocal(id)
    }

    override suspend fun markCheckInSynced(localId: String, serverId: String) {
        activityDao.markCheckInIsSyncInServer(localId, serverId)
    }

    override suspend fun markCheckOutDone(id: String) {
        activityDao.markCheckOutIsDoneInLocal(id)
    }

    override suspend fun markCheckOutSynced(id: String) {
        activityDao.markCheckOutIsSyncInServer(id)
    }

    override suspend fun countStillCheckIn(): Int {
        return activityDao.countStillCheckIn()
    }

    override suspend fun updateCheckOut(
        id: String,
        endTime: String,
        latitude: String,
        longitude: String,
    ) {
        activityDao.updateActivity(
            idActivity = id,
            checkOutTime = endTime,
            latitude = latitude,
            longitude = longitude
        )
    }

    override suspend fun insertLog(title: String, desc: String) {
        activityDao.insertLogActivity(
            LogEntity(
                title = title,
                description = desc,
                typeFeature = "Aktifitas"
            )
        )
    }

    override suspend fun getSyncStatus(trno: String): Int = activityDao.getSyncData(trno)
}
