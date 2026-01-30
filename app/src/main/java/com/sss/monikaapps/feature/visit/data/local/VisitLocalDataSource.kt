package com.sss.monikaapps.feature.visit.data.local

import com.sss.monikaapps.feature.visit.data.entity.VisitEntity

interface VisitLocalDataSource {
    suspend fun fetchDataVisit(): List<VisitEntity>
    suspend fun fetchDetailVisit(idVisit : String) : VisitEntity
}