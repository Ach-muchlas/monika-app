package com.sss.monikaapps.feature.utils.device.domain.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

class DeviceInfoRepositoryImpl(
    private val context: Context
) : DeviceInfoRepository {

    @SuppressLint("HardwareIds")
    override suspend fun getDeviceId(): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "-"
    }

    override suspend fun getAppVersion(): String {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return "${packageInfo.versionName}"
    }
}
