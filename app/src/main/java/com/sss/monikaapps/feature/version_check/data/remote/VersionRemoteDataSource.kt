package com.sss.monikaapps.feature.version_check.data.remote

import com.sss.monikaapps.feature.version_check.data.response.VersionResponse

interface VersionRemoteDataSource {
    suspend fun fetchVersionAppsInServer(): VersionResponse?
}