package com.sss.monikaapps.feature.visit.domain.repository

import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import com.sss.monikaapps.feature.visit.domain.model.CheckInVisitRequest
import com.sss.monikaapps.feature.visit.domain.model.CheckOutVisitRequest

interface VisitRepository {
    suspend fun fetchVisitLocalDatabase(keyword : String? , status :Int?): List<VisitEntity>
    suspend fun fetchVisitNotSync(): List<VisitEntity>
    suspend fun fetchVisitDetail(idVisit: String): VisitEntity
    suspend fun countStillCheckIn(): Int
    suspend fun checkInVisitRemote(payload: CheckInVisitRequest): String?

    suspend fun checkInVisit(
        idVisit: String, desc: String, timeCheckIn: String, startLat: String, startLng: String,
    )

    suspend fun markCheckInIsSyncVisit(idVisit: String, trno: String)
    suspend fun markCheckOutIsDoneVisit(idVisit: String)
    suspend fun markCheckOutIsSyncVisit(idVisit: String)

    suspend fun checkOutVisitRemote(trno: String, payload: CheckOutVisitRequest): String?

    suspend fun checkOutVisit(
        idVisit: String, timeCheckOut: String, endLat: String, endLng: String,
    )

    suspend fun fetchPhotosByFeatureId(parentId: String, feature: String): List<PhotoEntity>
    suspend fun fetchPhotoByParentId(parentId: String): List<PhotoEntity>

    suspend fun insertLogActivities(title: String, desc: String)

     fun calculateDistanceInMeters(
        customerLat: String,
        customerLong: String,
        userLat: Double,
        userLong: Double,
    ): Double

}