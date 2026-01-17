package com.sss.monikaapps.common.navigation

import androidx.navigation.NavController

fun NavController.navigateToDestination(
    destination: RouteDestination,
) {
    when (destination) {

        RouteDestination.LoginToHome -> {
            navigate(Routes.HOME) {
                popUpTo(Routes.LOGIN) { inclusive = true }
                launchSingleTop = true
            }
        }

        RouteDestination.HomeToActivities -> {
            navigate(Routes.ACTIVITIES) { launchSingleTop = true }
        }

        RouteDestination.HomeToExpanses -> {
            navigate(Routes.EXPANSES) { launchSingleTop = true }
        }


        is RouteDestination.ActivityToCreateActivity -> {
            navigate(Routes.createActivityOrUpdate(destination.typeActivity)) {
                launchSingleTop = true
            }
        }

        is RouteDestination.ActivityToDetail -> {
            navigate(
                Routes.detailActivities(
                    destination.trno,
                    destination.idMobile,
                    destination.locationData
                )
            ) {
                launchSingleTop = true
            }
        }

        is RouteDestination.DetailActivityToCheckOutActivity -> {
            navigate(
                Routes.checkOutActivities(
                    destination.trno,
                    destination.idMobile,
                    destination.typeActivity
                )
            ) {
                launchSingleTop = true
            }
        }

        is RouteDestination.ExpansesToDetailExpanse -> {
            navigate(Routes.expanseToDetailExpanse(destination.trno)) {
                launchSingleTop = true
            }
        }
    }
}

