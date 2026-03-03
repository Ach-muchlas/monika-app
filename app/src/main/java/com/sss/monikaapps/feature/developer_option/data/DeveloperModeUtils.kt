package com.sss.monikaapps.feature.developer_option.data

import android.content.Context
import android.provider.Settings

object DeveloperModeUtils {

    fun isDeveloperModeEnabled(context: Context): Boolean {
        return try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                0
            ) == 1
        } catch (e: Exception) {
            false
        }
    }
}
