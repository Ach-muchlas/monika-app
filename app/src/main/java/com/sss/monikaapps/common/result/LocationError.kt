package com.sss.monikaapps.common.result

sealed class LocationError(val code: String) {
    data object NoPermission : LocationError("NO_PERMISSION")
    data object Timeout : LocationError("TIMEOUT")
    data object NoLocation : LocationError("NO_LOCATION")
    data object Failed : LocationError("FAILED")
}
