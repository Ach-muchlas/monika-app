package com.sss.monikaapps.feature.utils.device.domain.repository

interface DeviceInfoRepository {
    suspend fun getDeviceId(): String
    suspend fun getAppVersion(): String
}
