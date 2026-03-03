package com.sss.monikaapps.common.navigation

sealed class RouteDestination {

    data object LoginToHome : RouteDestination()
    data object LoginToConnection : RouteDestination()

    data object HomeToActivities : RouteDestination()
    data object HomeToExpanses : RouteDestination()
    data object HomeToVisit : RouteDestination()
    data object HomeToDownload : RouteDestination()
    data object HomeToMaster : RouteDestination()
    data object HomeToInvoice : RouteDestination()
    data object HomeToSetting : RouteDestination()

    data object ConnectionToLogin : RouteDestination()

    data class ActivityToCreateActivity(val typeActivity: String) : RouteDestination()

    data class DetailActivityToCheckOutActivity(
        val trno: String,
        val idMobile: String,
        val typeActivity: String,
    ) : RouteDestination()

    data class ActivityToDetail(
        val trno: String,
        val idMobile: String,
    ) : RouteDestination()

    data class ExpensesToExpenseDetail(val trno: String) : RouteDestination()
    data object ExpenseToCreateHeaderExpense : RouteDestination()
    data class ExpenseDetailToCreateExpenseDetail(val trno: String) : RouteDestination()
    data class ExpenseDetailToUpdateExpenseDetail(
        val trno: String,
        val idExpense: String,
        val netAmount: String,
        val note: String,
        val initKm: String,
        val finalKm: String,
    ) : RouteDestination()

    data class VisitToDetailVisit(
        val idVisit: String,
    ) : RouteDestination()

    data class VisitDetailToCheckInVisit(val idVisit: String, val type: String) : RouteDestination()

    data object SettingToConnection : RouteDestination()
    data object SettingToLogin : RouteDestination()
    data object SettingToResultDownload : RouteDestination()

    data object MasterDataToMasterDataExpense : RouteDestination()
    data class ToDetailPhoto(val urlPhoto: String) : RouteDestination()
}
