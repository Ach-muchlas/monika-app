package com.sss.monikaapps.feature.download.data.remote

import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse
import com.sss.monikaapps.feature.visit.domain.model.CheckInVisitRequest

interface DownloadRemoteDataSource {
    suspend fun fetchDownloadVisit(): VisitDownloadResponse?
}