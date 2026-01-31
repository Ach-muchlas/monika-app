package com.sss.monikaapps.common.navigation

sealed class RouteDestination {

    data object LoginToHome : RouteDestination()

    data object HomeToActivities : RouteDestination()
    data object HomeToExpanses : RouteDestination()
    data object HomeToVisit : RouteDestination()
    data object HomeToDownload : RouteDestination()

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

    data class ExpensesToExpenseDetail(val trno: String) : RouteDestination()
    data object ExpenseToCreateHeaderExpense : RouteDestination()
    data class ExpenseDetailToCreateExpenseDetail(val trno: String) : RouteDestination()
    data class ExpenseDetailToUpdateExpenseDetail(
        val trno: String,
        val idExpense: String,
        val netAmount: String,
        val note: String,
    ) : RouteDestination()

    data class VisitToDetailVisit(
        val idVisit: String,
    ) : RouteDestination()

    data class VisitDetailToCheckInVisit(val idVisit: String, val type : String) : RouteDestination()
}
