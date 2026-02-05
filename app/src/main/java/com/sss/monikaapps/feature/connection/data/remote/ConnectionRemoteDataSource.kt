package com.sss.monikaapps.feature.connection.data.remote

import com.sss.monikaapps.feature.connection.data.response.VersionResponse

interface ConnectionRemoteDataSource {
    suspend fun fetchVersionAppsInServer(): VersionResponse?
}