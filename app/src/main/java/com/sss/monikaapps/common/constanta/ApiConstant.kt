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
    private const val VISIT = "kunjungan"
    private const val MASTER = "master"
    private const val HEADER = "hdr"
    private const val DETAIL = "dtl"

    const val AUTH = "user-login"
    const val APP_VERSION = "app-version"
    const val FETCH_DATA_ACTIVITIES = "${ACTIVITIES}-get"
    const val FETCH_DETAIL_ACTIVITY = "${ACTIVITIES}-detail"
    const val CHECK_IN_ACTIVITY = "${ACTIVITIES}-checkin"
    const val CHECK_OUT_ACTIVITY = "${ACTIVITIES}-checkout"

    const val FETCH_DATA_EXPENSES = "${EXPANSE}-get"
    const val SUBMIT_EXPENSE = "${EXPANSE}-ajukan"
    const val UN_SUBMIT_EXPENSE = "${EXPANSE}-batal-ajukan"
    const val FETCH_DETAIL_EXPENSE = "${EXPANSE}-detail"
    const val CREATE_HEADER_EXPENSE = "${EXPANSE}-${HEADER}-add"
    const val DELETE_HEADER_EXPENSE = "${EXPANSE}-${HEADER}-delete"
    const val CREATE_DETAIL_EXPENSE = "${EXPANSE}-${DETAIL}-add"
    const val UPDATE_DETAIL_EXPENSE = "${EXPANSE}-${DETAIL}-edit"
    const val DELETE_DETAIL_EXPENSE = "${EXPANSE}-${DETAIL}-delete"

    const val FETCH_DOWNLOAD_VISIT = "${VISIT}-download"
    const val CHECK_DATA_DOWNLOAD_VISIT = "${VISIT}-check-data"
    const val CHECK_IN_VISIT = "${VISIT}-checkin"
    const val CHECK_OUT_VISIT = "${VISIT}-checkout"

    const val FETCH_MASTERING_EXPENSE = "${MASTER}-${EXPANSE}-get"
    const val SEND_EMAIL = "send-export-to-email"
}