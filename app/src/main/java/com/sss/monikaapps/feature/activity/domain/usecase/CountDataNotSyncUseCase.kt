package com.sss.monikaapps.feature.activity.domain.usecase

import com.sss.monikaapps.feature.activity.domain.repository.ActivitiesRepository
import kotlinx.coroutines.flow.Flow

class CountDataNotSyncUseCase(private val repository: ActivitiesRepository) {
    operator fun invoke(): Flow<Int> = repository.countDataCheckoutNotSync()

}