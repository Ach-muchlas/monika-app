package com.sss.monikaapps.feature.activity.utils

import android.content.Context
import com.sss.monikaapps.common.helper.NetworkHelper.isInternetAvailable

class NetworkCheckerImpl(private val context: Context) : NetworkChecker {
    override fun isConnected(): Boolean = isInternetAvailable(context)
}
