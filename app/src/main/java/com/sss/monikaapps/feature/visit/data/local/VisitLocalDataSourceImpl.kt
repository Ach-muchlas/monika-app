package com.sss.monikaapps.feature.visit.data.local

import com.sss.monikaapps.common.db.dao.PhotoDao
import com.sss.monikaapps.common.db.entity.LogEntity
import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.feature.visit.data.dao.VisitDao
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity

class VisitLocalDataSourceImpl(private val dao: VisitDao, private val photoDao: PhotoDao) :
    VisitLocalDataSource {

    override suspend fun fetchDataVisit(keyword: String?, status: Int?): List<VisitEntity> {
        return dao.fetchVisitLocalDatabase(keyword, status)
    }

    override suspend fun fetchDataVisitNotSync(): List<VisitEntity> {
        return dao.fetchVisitNotSync()
    }

    override suspend fun fetchDetailVisit(idVisit: String): VisitEntity {
        return dao.fetchVisitDetail(idVisit)
    }

    override suspend fun countStillCheckIn(): Int {
        return dao.countStillCheckIn()
    }

    override suspend fun checkInVisit(
        idVisit: String,
        desc: String,
        timeCheckIn: String,
        startLat: String,
        startLng: String,
    ) {
        return dao.checkInVisit(
            idVisit = idVisit,
            desc = desc,
            timeCheckIn = timeCheckIn,
            startLat = startLat,
            starLng = startLng
        )
    }

    override suspend fun checkOutVisit(
        idVisit: String,
        timeCheckOut: String,
        endLat: String,
        endLng: String,
    ) {
        return dao.checkOutVisit(
            idVisit = idVisit,
            timeCheckOut = timeCheckOut,
            endLat = endLat,
            endLng = endLng
        )
    }

    override suspend fun fetchPhotosByFeatureId(
        parentId: String,
        feature: String,
    ): List<PhotoEntity> {
        return photoDao.fetchPhotoByParentIdAndParentType(parentId = parentId, parentType = feature)
    }

    override suspend fun fetchPhotoByParentId(parentId: String): List<PhotoEntity> {
        return photoDao.fetchPhotosByParentId(parentId)
    }

    override suspend fun insertLogVisit(title: String, desc: String) {
        dao.insertLogVisit(
            LogEntity(
                title = title,
                description = desc,
                typeFeature = "Kunjungan"
            )
        )
    }

    override suspend fun markCheckInIsSync(idVisit: String, trno: String) {
        dao.markCheckInIsSyncInServer(idMobile = idVisit, trno = trno)
    }

    override suspend fun markCheckOutIsDoneVisit(idVisit: String) {
        dao.markCheckOutIsDoneInLocal(idVisit)
    }

    override suspend fun markCheckOutIsSync(idVisit: String) {
        dao.markCheckOutIsSyncInServer(idMobile = idVisit)
    }

}