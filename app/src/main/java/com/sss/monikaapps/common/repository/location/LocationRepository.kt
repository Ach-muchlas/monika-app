package com.sss.monikaapps.common.repository.location

import com.sss.monikaapps.utils.result.Result

interface LocationRepository {
    suspend fun getUserLocation(timeout: Long = 20_000L): Result<Pair<Double, Double>>
}