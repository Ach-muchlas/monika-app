package com.sss.monikaapps.common.data

import com.sss.monikaapps.common.constanta.ServerConstant.BASE_URL_OFFICE
import com.sss.monikaapps.common.constanta.ServerConstant.BASE_URL_PUBLIC

data class UrlModel(
    val urlName: String,
    val urlValue: String,
)

val listUrl = listOf(
    UrlModel("Public", BASE_URL_PUBLIC),
    UrlModel("Office", BASE_URL_OFFICE),
    UrlModel("Testing", "http://192.168.20.174:80"),
    UrlModel("Lainnya", "")
)