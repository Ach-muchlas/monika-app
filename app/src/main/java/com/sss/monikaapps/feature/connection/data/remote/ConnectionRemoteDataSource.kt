package com.sss.monikaapps.feature.connection.data.remote

import com.sss.monikaapps.feature.connection.data.response.CheckConnectionResponse

interface ConnectionRemoteDataSource {
    suspend fun fetchCheckConnectionDatabase(): CheckConnectionResponse?
}