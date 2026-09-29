package com.sss.monikaapps.feature.login.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("employee_id")
    val employeeId: String,
    val imei: String,
    @SerializedName("app_version")
    val versionApps : String
)
