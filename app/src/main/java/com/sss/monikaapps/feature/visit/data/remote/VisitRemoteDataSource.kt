package com.sss.monikaapps.feature.visit.data.remote

import com.sss.monikaapps.feature.visit.domain.model.CheckInVisitRequest
import com.sss.monikaapps.feature.visit.domain.model.CheckOutVisitRequest

interface VisitRemoteDataSource {
    suspend fun checkInVisit(
        payload: CheckInVisitRequest,
    ): String?

    suspend fun checkOutVisit(
        trno: String,
        payload: CheckOutVisitRequest,
    ): String?
}