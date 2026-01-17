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
}