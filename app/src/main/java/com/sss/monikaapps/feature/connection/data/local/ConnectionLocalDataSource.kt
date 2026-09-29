package com.sss.monikaapps.feature.connection.data.local

interface ConnectionLocalDataSource {
    fun saveServerUrl(url: String)
    fun fetchServerUrl() : String
}