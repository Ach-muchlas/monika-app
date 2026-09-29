package com.sss.monikaapps.feature.connection.data.local

import com.sss.monikaapps.common.manager.ServerManager

class ConnectionLocalDataSourceImpl(private val serverManager: ServerManager) :
    ConnectionLocalDataSource {
    override fun saveServerUrl(url: String) {
        serverManager.setServerAddress(url)
    }

    override fun fetchServerUrl(): String = serverManager.getServerAddress()
}