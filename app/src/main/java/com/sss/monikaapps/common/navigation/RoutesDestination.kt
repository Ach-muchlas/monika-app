package com.sss.monikaapps.common.navigation

sealed class RouteDestination {

    data object LoginToHome : RouteDestination()

    data object HomeToActivities : RouteDestination()
    data object HomeToExpanses : RouteDestination()

    data class ActivityToCreateActivity(val typeActivity: String) : RouteDestination()

    data class DetailActivityToCheckOutActivity(
        val trno: String,
        val idMobile: String,
        val typeActivity: String,
    ) : RouteDestination()

    data class ActivityToDetail(
        val trno: String,
        val idMobile: String,
        val locationData: Int,
    ) : RouteDestination()

    data class ExpansesToDetailExpanse(val trno: String) : RouteDestination()
}
