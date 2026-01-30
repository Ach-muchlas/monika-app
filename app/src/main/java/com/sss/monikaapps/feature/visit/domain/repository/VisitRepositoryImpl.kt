package com.sss.monikaapps.feature.visit.domain.repository

import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import com.sss.monikaapps.feature.visit.data.local.VisitLocalDataSource

class VisitRepositoryImpl(private val local: VisitLocalDataSource) : VisitRepository {
    override suspend fun fetchVisitLocalDatabase(): List<VisitEntity> {
        return local.fetchDataVisit()
    }

    override suspend fun fetchVisitDetail(idVisit: String): VisitEntity {
        return local.fetchDetailVisit(idVisit)
    }
}