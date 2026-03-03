package com.sss.monikaapps.feature.version_check.domain.repository

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.version_check.data.remote.VersionRemoteDataSource
import com.sss.monikaapps.feature.version_check.data.response.VersionResponse

class VersionRepositoryImpl(
    private val remote: VersionRemoteDataSource,
) : VersionRepository {
    override suspend fun fetchVersionApps(): Result<VersionResponse> {
        return try {
            Result.loading(null)
            val response = remote.fetchVersionAppsInServer()
            Result.success(response)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!")
        }
    }
}