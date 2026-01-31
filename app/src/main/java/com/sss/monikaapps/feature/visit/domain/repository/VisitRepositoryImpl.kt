package com.sss.monikaapps.feature.visit.domain.repository

import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import com.sss.monikaapps.feature.visit.data.local.VisitLocalDataSource
import com.sss.monikaapps.feature.visit.data.remote.VisitRemoteDataSource
import com.sss.monikaapps.feature.visit.domain.model.CheckInVisitRequest
import com.sss.monikaapps.feature.visit.domain.model.CheckOutVisitRequest

class VisitRepositoryImpl(
    private val local: VisitLocalDataSource,
    private val remote: VisitRemoteDataSource,
) : VisitRepository {
    override suspend fun fetchVisitLocalDatabase(): List<VisitEntity> {
        return local.fetchDataVisit()
    }

    override suspend fun fetchVisitDetail(idVisit: String): VisitEntity {
        return local.fetchDetailVisit(idVisit)
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
}