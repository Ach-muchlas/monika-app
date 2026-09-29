package com.sss.monikaapps.common.result

import com.sss.monikaapps.common.data.StatusNetwork

data class Result<out T>(
    val status: StatusNetwork,
    val data: T?,
    val message: String?,
    val progress: Float = 0f
) {
    companion object {
        fun <T> loading(data: T?, progress: Float = 0f, message: String? = null): Result<T> =
            Result(status = StatusNetwork.LOADING, data = data, message = message, progress = progress)

        fun <T> success(data: T?) =
            Result(status = StatusNetwork.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String) =
            Result(status = StatusNetwork.ERROR, data = data, message = message)
    }
}