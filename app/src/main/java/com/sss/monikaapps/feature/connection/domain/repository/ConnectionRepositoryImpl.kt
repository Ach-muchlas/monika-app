package com.sss.monikaapps.feature.connection.domain.repository

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.connection.data.local.ConnectionLocalDataSource
import com.sss.monikaapps.feature.connection.data.remote.ConnectionRemoteDataSource
import com.sss.monikaapps.feature.connection.data.response.VersionResponse
import com.sss.monikaapps.network.ApiConfig

class ConnectionRepositoryImpl(
    private val remote: ConnectionRemoteDataSource,
    private val local: ConnectionLocalDataSource,
) : ConnectionRepository {
    override suspend fun changeServer(serverUrl: String): Result<VersionResponse> {
        return try {
            local.saveServerUrl(serverUrl)

            ApiConfig.reloadApiService()

            val data = remote.fetchVersionAppsInServer()
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

}