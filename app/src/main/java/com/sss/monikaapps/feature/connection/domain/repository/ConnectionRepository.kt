package com.sss.monikaapps.feature.connection.domain.repository

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.connection.data.response.CheckConnectionResponse
import com.sss.monikaapps.feature.version_check.data.response.VersionResponse

interface ConnectionRepository {
    suspend fun changeServer(serverUrl: String): Result<CheckConnectionResponse>
    fun fetchServerAddress(): Result<String>
    suspend fun fetchCheckConnectionDatabase(): Result<CheckConnectionResponse>
}