package com.sss.monikaapps.common.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.sss.monikaapps.common.constanta.ServerConstant

class ServerManager private constructor() {
    private lateinit var sharePref: SharedPreferences

    fun init(context: Context) {
        sharePref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setServerAddress(serverUrl: String) {
        if (serverUrl.isNotBlank()) {
            sharePref.edit { putString(KEY_SERVER_MONIKA, serverUrl) }
        }
    }

    fun getServerAddress(): String {
        return sharePref.getString(KEY_SERVER_MONIKA, DEFAULT_SERVER_S3GO_ATOSSS)
            ?: DEFAULT_SERVER_S3GO_ATOSSS
    }

    companion object {
        private const val PREF_NAME = "server_pref"
        private const val KEY_SERVER_MONIKA = "server_monika"
        private const val DEFAULT_SERVER_S3GO_ATOSSS = ServerConstant.BASE_URL_PUBLIC

        @Volatile
        private var instance: ServerManager? = null

        fun getInstance(): ServerManager {
            return instance ?: synchronized(this) {
                instance ?: ServerManager().also { instance = it }
            }
        }
    }
}