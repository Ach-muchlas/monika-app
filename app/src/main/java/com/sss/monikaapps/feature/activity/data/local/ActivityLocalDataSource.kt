package com.sss.monikaapps.feature.activity.data.local

import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity

interface ActivityLocalDataSource {
    suspend fun insertActivity(entity: ActivityEntity)
    suspend fun getDetail(id: String): ActivityEntity
    suspend fun getPhotos(id: String): List<PhotoEntity>

    suspend fun markCheckInDone(id: String)
    suspend fun markCheckInSynced(localId: String, serverId: String)

    suspend fun updateCheckOut(id: String, endTime: String, latitude: String, longitude: String)
    suspend fun markCheckOutDone(id: String)
    suspend fun markCheckOutSynced(id: String)

    suspend fun countStillCheckIn(): Int

    suspend fun insertLog(title: String, desc: String)
}
