package com.sss.monikaapps.feature.result_download.domain.usecase

import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.result_download.domain.repository.BackupRemoteRepository
import java.io.File

class SendEmailUseCase(private val repository: BackupRemoteRepository) {
    suspend operator fun invoke(
        reason: String,
        file: File,
    ): Result<DefaultAddResponse> {
        return try {
            val sendEmail = repository.sendEmail(reason, file)
            Result.success(sendEmail)
        } catch (e: Exception) {
            Result.error(null, e.message ?: "Error Occurred!!")
        }
    }

}