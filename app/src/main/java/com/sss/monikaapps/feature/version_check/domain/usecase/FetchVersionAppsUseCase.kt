package com.sss.monikaapps.feature.version_check.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.version_check.data.response.VersionResponse
import com.sss.monikaapps.feature.version_check.domain.repository.VersionRepository

class FetchVersionAppsUseCase(
    private val repository: VersionRepository
) {
    suspend operator fun invoke(): Result<VersionResponse> {
        return repository.fetchVersionApps()
    }
}
