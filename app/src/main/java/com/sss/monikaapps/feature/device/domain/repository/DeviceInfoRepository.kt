package com.sss.monikaapps.feature.device.domain.repository

interface DeviceInfoRepository {
    suspend fun getDeviceId(): String
    suspend fun getAppVersion(): String
    suspend fun getSystemOperation (): String
}
