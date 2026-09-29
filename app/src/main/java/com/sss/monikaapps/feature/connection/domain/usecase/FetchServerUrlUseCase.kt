package com.sss.monikaapps.feature.connection.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.connection.domain.repository.ConnectionRepository

class FetchServerUrlUseCase(
    private val repository: ConnectionRepository,
) {
    suspend operator fun invoke(): Result<String> {
        return repository.fetchServerAddress()
    }
}
