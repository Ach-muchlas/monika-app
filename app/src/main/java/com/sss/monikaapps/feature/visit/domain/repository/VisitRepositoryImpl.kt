package com.sss.monikaapps.feature.visit.domain.repository

import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import com.sss.monikaapps.feature.visit.data.local.VisitLocalDataSource
import com.sss.monikaapps.feature.visit.data.remote.VisitRemoteDataSource
import com.sss.monikaapps.feature.visit.domain.model.CheckInVisitRequest
import com.sss.monikaapps.feature.visit.domain.model.CheckOutVisitRequest
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class VisitRepositoryImpl(
    private val local: VisitLocalDataSource,
    private val remote: VisitRemoteDataSource,
) : VisitRepository {
    override suspend fun fetchVisitLocalDatabase(keyword : String? , status :Int?): List<VisitEntity> {
        return local.fetchDataVisit(keyword, status)
    }

    override suspend fun fetchVisitNotSync(): List<VisitEntity> {
        return local.fetchDataVisitNotSync()
    }

    override suspend fun fetchVisitDetail(idVisit: String): VisitEntity {
        return local.fetchDetailVisit(idVisit)
    }

    override suspend fun countStillCheckIn(): Int {
        return local.countStillCheckIn()
    }

    override suspend fun checkInVisitRemote(payload: CheckInVisitRequest): String? {
        return remote.checkInVisit(payload)
    }

    override suspend fun checkOutVisitRemote(trno: String, payload: CheckOutVisitRequest): String? {
        return remote.checkOutVisit(trno, payload)
    }

    override suspend fun checkInVisit(
        idVisit: String, desc: String, timeCheckIn: String, startLat: String, startLng: String,
    ) {
        local.checkInVisit(idVisit, desc, timeCheckIn, startLat, startLng)
    }

    override suspend fun markCheckInIsSyncVisit(idVisit: String, trno: String) {
        return local.markCheckInIsSync(idVisit, trno)
    }

    override suspend fun markCheckOutIsDoneVisit(idVisit: String) {
        return local.markCheckOutIsDoneVisit(idVisit)
    }

    override suspend fun markCheckOutIsSyncVisit(idVisit: String) {
        return local.markCheckOutIsSync(idVisit)
    }

    override suspend fun checkOutVisit(
        idVisit: String,
        timeCheckOut: String,
        endLat: String,
        endLng: String,
    ) {
        local.checkOutVisit(idVisit, timeCheckOut, endLat, endLng)
    }

    override suspend fun fetchPhotosByFeatureId(
        parentId: String,
        feature: String,
    ): List<PhotoEntity> = local.fetchPhotosByFeatureId(parentId, feature)

    override suspend fun fetchPhotoByParentId(parentId: String): List<PhotoEntity> =
        local.fetchPhotoByParentId(parentId)

    override suspend fun insertLogActivities(title: String, desc: String) {
        local.insertLogVisit(title, desc)
    }

    override fun calculateDistanceInMeters(
        customerLat: String,
        customerLong: String,
        userLat: Double,
        userLong: Double,
    ): Double {
        val gpsLat = customerLat.toDoubleOrNull() ?: 0.0
        val gpsLong = customerLong.toDoubleOrNull() ?: 0.0

        return if (gpsLat != 0.0 || gpsLong != 0.0) {
            val distance = calculateDistance(userLat, userLong, gpsLat, gpsLong)
            BigDecimal(distance).setScale(2, RoundingMode.HALF_UP).toDouble()
        } else {
            0.0
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c * 1000
    }
}