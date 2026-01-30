package com.sss.monikaapps.feature.visit.data.local

import com.sss.monikaapps.feature.visit.data.dao.VisitDao
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity

class VisitLocalDataSourceImpl(private val dao: VisitDao) : VisitLocalDataSource {
    override suspend fun fetchDataVisit(): List<VisitEntity> {
        return dao.fetchVisitLocalDatabase()
    }

    override suspend fun fetchDetailVisit(idVisit: String): VisitEntity {
        return dao.fetchVisitDetail(idVisit)
    }

}