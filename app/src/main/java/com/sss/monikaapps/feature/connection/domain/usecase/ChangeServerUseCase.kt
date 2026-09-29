package com.sss.monikaapps.feature.connection.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.connection.data.response.CheckConnectionResponse
import com.sss.monikaapps.feature.version_check.data.response.VersionResponse
import com.sss.monikaapps.feature.connection.domain.repository.ConnectionRepository

class ChangeServerUseCase(
    private val repository: ConnectionRepository,
) {
    suspend operator fun invoke(serverUrl: String): Result<CheckConnectionResponse> {
        return repository.changeServer(serverUrl)
    }
}
