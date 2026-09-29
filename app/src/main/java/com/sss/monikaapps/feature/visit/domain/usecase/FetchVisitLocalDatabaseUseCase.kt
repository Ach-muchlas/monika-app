package com.sss.monikaapps.feature.visit.domain.usecase

import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import com.sss.monikaapps.feature.visit.domain.repository.VisitRepository

class FetchVisitLocalDatabaseUseCase(private val repository: VisitRepository) {
    suspend operator fun invoke(keyword : String? , status :Int?): Result<List<VisitEntity>> {
        return try {
            val data = repository.fetchVisitLocalDatabase(keyword, status)
            Result.success(data)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Gagal mengambil aktivitas")
        }
    }
}