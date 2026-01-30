package com.sss.monikaapps.feature.visit.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import com.sss.monikaapps.feature.visit.domain.repository.VisitRepository

class FetchVisitDetailUseCase(private val repository: VisitRepository) {
    suspend operator fun invoke(idVisit: String): Result<VisitEntity> {
        return try {
            val data = repository.fetchVisitDetail(idVisit)
            Result.success(data)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!!")
        }
    }
}