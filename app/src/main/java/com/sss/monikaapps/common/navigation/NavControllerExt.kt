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

        RouteDestination.HomeToDownload -> {
            navigate(Routes.DOWNLOAD) { launchSingleTop = true }
        }

        RouteDestination.HomeToSetting -> {
            navigate(Routes.SETTING) { launchSingleTop = true }
        }

        RouteDestination.HomeToMaster -> {
            navigate(Routes.MASTER_DATA) { launchSingleTop = true }
        }

        RouteDestination.ConnectionToLogin -> {
            navigate(Routes.LOGIN) {
                popUpTo(Routes.CONNECTION) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        RouteDestination.HomeToVisit -> {
            navigate(Routes.VISIT) { launchSingleTop = true }
        }


        is RouteDestination.ActivityToCreateActivity -> {
            navigate(Routes.createActivityOrUpdate(destination.typeActivity)) {
                launchSingleTop = true
            }
        }

        is RouteDestination.ActivityToDetail -> {
            navigate(
                Routes.detailActivities(
                    destination.trno, destination.idMobile, destination.locationData
                )
            ) {
                launchSingleTop = true
            }
        }

        is RouteDestination.DetailActivityToCheckOutActivity -> {
            navigate(
                Routes.checkOutActivities(
                    destination.trno, destination.idMobile, destination.typeActivity
                )
            ) {
                launchSingleTop = true
            }
        }

        is RouteDestination.ExpensesToExpenseDetail -> {
            navigate(Routes.expenseToDetailExpense(destination.trno)) {
                launchSingleTop = true
            }
        }

        RouteDestination.ExpenseToCreateHeaderExpense -> {
            navigate(Routes.CREATE_EXPENSE_HEADER) { launchSingleTop = true }
        }

        is RouteDestination.ExpenseDetailToCreateExpenseDetail -> {
            navigate(Routes.createExpenseDetail(destination.trno)) { launchSingleTop = true }
        }

        is RouteDestination.ExpenseDetailToUpdateExpenseDetail -> {
            navigate(
                Routes.updateExpenseDetail(
                    destination.trno,
                    destination.idExpense,
                    destination.netAmount,
                    destination.note,
                    destination.initKm,
                    destination.finalKm
                )
            ) { launchSingleTop = true }
        }

        is RouteDestination.VisitToDetailVisit -> {
            navigate(Routes.detailVisit(destination.idVisit)) { launchSingleTop = true }
        }

        is RouteDestination.VisitDetailToCheckInVisit -> {
            navigate(Routes.checkInVisit(destination.idVisit, destination.type)) {
                launchSingleTop = true
            }
        }

        RouteDestination.SettingToConnection -> {
            navigate(Routes.CONNECTION_SETTING) { launchSingleTop = true }
        }

        RouteDestination.SettingToResultDownload -> {
            navigate(Routes.RESULT_DOWNLOAD) { launchSingleTop = true }
        }

        RouteDestination.SettingToLogin -> {
            navigate(Routes.LOGIN) {
                popUpTo(graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        RouteDestination.LoginToConnection -> {
            navigate(Routes.CONNECTION) { launchSingleTop = true }
        }

        RouteDestination.MasterDataToMasterDataExpense -> {
            navigate(Routes.MASTER_DATA_EXPENSE) { launchSingleTop = true }
        }
    }

}

