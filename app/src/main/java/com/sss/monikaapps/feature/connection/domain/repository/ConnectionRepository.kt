package com.sss.monikaapps.feature.connection.domain.repository

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.version_check.data.response.VersionResponse

interface ConnectionRepository {
    suspend fun changeServer(serverUrl: String): Result<VersionResponse>
    fun fetchServerAddress(): Result<String>
}