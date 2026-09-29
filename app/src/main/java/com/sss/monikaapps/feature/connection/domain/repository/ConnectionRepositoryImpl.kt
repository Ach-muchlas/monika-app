package com.sss.monikaapps.feature.connection.domain.repository

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.connection.data.local.ConnectionLocalDataSource
import com.sss.monikaapps.feature.connection.data.remote.ConnectionRemoteDataSource
import com.sss.monikaapps.feature.connection.data.response.CheckConnectionResponse
import com.sss.monikaapps.feature.version_check.data.remote.VersionRemoteDataSource
import com.sss.monikaapps.feature.version_check.data.response.VersionResponse
import com.sss.monikaapps.network.ApiConfig

class ConnectionRepositoryImpl(
    private val remote: ConnectionRemoteDataSource,
    private val local: ConnectionLocalDataSource,
) : ConnectionRepository {
    override suspend fun changeServer(serverUrl: String): Result<CheckConnectionResponse> {
        return try {
            local.saveServerUrl(serverUrl)

            ApiConfig.reloadApiService()

            val data = remote.fetchCheckConnectionDatabase()
            Result.success(data)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred")
        }
    }

    override fun fetchServerAddress(): Result<String> {
        return try {
            Result.success(local.fetchServerUrl())
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred")
        }
    }

    override suspend fun fetchCheckConnectionDatabase(): Result<CheckConnectionResponse> {
        return try {
            Result.success(remote.fetchCheckConnectionDatabase())
        } catch (e: Exception) {
            Result.error(null, e.message ?: "ERROR OCCURRED")
        }
    }

}