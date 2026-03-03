package com.sss.monikaapps.feature.version_check.domain.repository

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.version_check.data.response.VersionResponse

interface VersionRepository {
   suspend fun fetchVersionApps(): Result<VersionResponse>
}