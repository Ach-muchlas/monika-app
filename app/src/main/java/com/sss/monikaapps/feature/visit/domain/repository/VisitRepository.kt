package com.sss.monikaapps.feature.visit.domain.repository

import com.sss.monikaapps.feature.visit.data.entity.VisitEntity

interface VisitRepository {
    suspend fun fetchVisitLocalDatabase(): List<VisitEntity>
    suspend fun fetchVisitDetail(idVisit: String): VisitEntity
}