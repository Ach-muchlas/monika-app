package com.sss.monikaapps.feature.visit.data.local

import androidx.room.Query
import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity

interface VisitLocalDataSource {
    suspend fun fetchDataVisit(keyword : String?, status :Int?): List<VisitEntity>
    suspend fun fetchDataVisitNotSync(): List<VisitEntity>
    suspend fun fetchDetailVisit(idVisit: String): VisitEntity
    suspend fun countStillCheckIn(): Int
    suspend fun checkInVisit(
        idVisit: String,
        desc: String,
        timeCheckIn: String,
        startLat: String,
        startLng: String,
    )

    suspend fun checkOutVisit(
        idVisit: String,
        timeCheckOut: String,
        endLat: String,
        endLng: String,
    )

    suspend fun fetchPhotosByFeatureId(parentId: String, feature: String): List<PhotoEntity>
    suspend fun fetchPhotoByParentId(parentId: String): List<PhotoEntity>

    suspend fun insertLogVisit(title: String, desc: String)

    suspend fun markCheckInIsSync(idVisit: String, trno: String)
    suspend fun markCheckOutIsDoneVisit(idVisit: String)
    suspend fun markCheckOutIsSync(idVisit: String)
}