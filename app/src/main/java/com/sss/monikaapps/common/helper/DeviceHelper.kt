package com.sss.monikaapps.common.helper

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

object DeviceHelper {

    @SuppressLint("HardwareIds")
    fun getDeviceImei(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "-"
    }

    fun getApplicationVersion(context: Context): String {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return "V." + packageInfo.versionName
    }
}