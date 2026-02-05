package com.sss.monikaapps.feature.home.data.sealed

import com.sss.monikaapps.common.navigation.RouteDestination

sealed class HomeNavEvent {
    data class Navigate(val destination: RouteDestination) : HomeNavEvent()
    data class Blocked(val message: String) : HomeNavEvent()
}
