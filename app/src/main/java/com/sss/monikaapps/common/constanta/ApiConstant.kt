package com.sss.monikaapps.common.constanta

import com.sss.monikaapps.common.manager.ServerManager


object ApiConstant {

    fun urlDomain(): String {
        val serverAddress = ServerManager.getInstance().getServerAddress().trimEnd('/')
        return "$serverAddress/monika-app-backend/"
    }

    fun urlDomainForPhoto(): String {
        val serverAddress = ServerManager.getInstance().getServerAddress().trimEnd('/')
        return "$serverAddress/monika-app-backend/"
    }

    fun urlDomainForPhotoExternalProject(): String {
        val domain = when (ServerManager.getInstance().getServerAddress()) {
            ServerConstant.BASE_URL_OFFICE -> ServerConstant.BASE_URL_OFFICE
            else -> ServerConstant.BASE_URL_PUBLIC_PHOTO
        }
        val serverAddress = domain.trimEnd('/')
        return "$serverAddress/"
    }


    private const val ACTIVITIES = "aktivitas"
    private const val EXPANSE = "pengeluaran"
    private const val MASTER = "master"

    const val AUTH = "user-login"
    const val FETCH_DATA_ACTIVITIES = "${ACTIVITIES}-get"
    const val FETCH_DETAIL_ACTIVITY = "${ACTIVITIES}-detail"
    const val CHECK_IN_ACTIVITY = "${ACTIVITIES}-checkin"
    const val CHECK_OUT_ACTIVITY = "${ACTIVITIES}-checkout"

    const val FETCH_DATA_EXPANSES = "${EXPANSE}-get"
    const val FETCH_DETAIL_EXPANSE = "${EXPANSE}-detail"

    const val FETCH_MASTERING_EXPANSE = "${MASTER}-${EXPANSE}-get"

}