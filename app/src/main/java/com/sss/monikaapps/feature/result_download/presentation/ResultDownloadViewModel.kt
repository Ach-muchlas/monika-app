package com.sss.monikaapps.feature.result_download.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.database.AppDatabase
import com.sss.monikaapps.feature.result_download.domain.usecase.SendEmailUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ResultDownloadViewModel(
    private val sendEmailUseCase: SendEmailUseCase,
    private val database: AppDatabase,
) : ViewModel() {

    private val _sendEmailResult =
        MutableLiveData<Result<DefaultAddResponse>?>()
    val sendEmailResult: LiveData<Result<DefaultAddResponse>?> =
        _sendEmailResult

    fun exportAndSendDatabase(
        context: Context,
        reason: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _sendEmailResult.postValue(Result.loading(null))

                // 🔥 SINGLE SOURCE OF TRUTH
                val dbPath = database.openHelper.readableDatabase.path
                val dbFile = File(dbPath)

                if (!dbFile.exists()) {
                    _sendEmailResult.postValue(
                        Result.error(null, "Database tidak ditemukan")
                    )
                    return@launch
                }

                val tempFile = File(
                    context.cacheDir,
                    "backup_${System.currentTimeMillis()}.db"
                )

                dbFile.inputStream().use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                val result = sendEmailUseCase(reason, tempFile)
                _sendEmailResult.postValue(result)

            } catch (e: Exception) {
                _sendEmailResult.postValue(
                    Result.error(null, e.message ?: "Unknown error")
                )
            }
        }
    }

    fun clearResult() {
        _sendEmailResult.postValue(null)
    }

}
