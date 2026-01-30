package com.sss.monikaapps.feature.download.data.remote

import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse

interface DownloadRemoteDataSource {
    suspend fun fetchDownloadVisit(): VisitDownloadResponse?
}