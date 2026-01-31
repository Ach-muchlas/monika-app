package com.sss.monikaapps.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_EXPENSE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_MOBILE_ACTIVITY
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_VISIT
import com.sss.monikaapps.common.constanta.ArgumentsConstant.LOCATION_DATA
import com.sss.monikaapps.common.constanta.ArgumentsConstant.NET_AMOUNT
import com.sss.monikaapps.common.constanta.ArgumentsConstant.NOTE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TRNO_ACTIVITY
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TRNO_EXPENSE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TYPE_ACTIVITY
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TYPE_VISIT
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_ACTIVITIES
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_DOWNLOAD
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_EXPENSES
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_VISIT
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.feature.activity.ui.screen.ActivitiesScreen
import com.sss.monikaapps.feature.activity.ui.screen.ActivityDetailScreen
import com.sss.monikaapps.feature.activity.ui.screen.CreateActivityScreen
import com.sss.monikaapps.feature.download.presentation.DownloadScreen
import com.sss.monikaapps.feature.expense.presentation.create.CreateAndUpdateExpenseDetailScreen
import com.sss.monikaapps.feature.expense.presentation.create.CreateExpenseScreen
import com.sss.monikaapps.feature.expense.presentation.detail.ExpanseDetailScreen
import com.sss.monikaapps.feature.expense.presentation.list.ExpansesScreen
import com.sss.monikaapps.feature.home.ui.screen.HomeScreen
import com.sss.monikaapps.feature.login.ui.screen.LoginScreen
import com.sss.monikaapps.feature.visit.presentation.detail.VisitDetailScreen
import com.sss.monikaapps.feature.visit.presentation.list.VisitListScreen
import com.sss.monikaapps.feature.visit.presentation.update.UpdateVisitScreen


@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    val sessionManager = remember {
        SessionManager.getInstance()
    }

    val startDestination = remember {
        if (sessionManager.isUserLogin()) Routes.HOME else Routes.LOGIN
    }

    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onSuccessLoginSuccess = {
                    navController.navigateToDestination(
                        RouteDestination.LoginToHome
                    )
                })
        }

        composable(Routes.HOME) {
            HomeScreen { idMenu ->
                when (idMenu) {
                    FEATURE_ACTIVITIES -> navController.navigateToDestination(
                        RouteDestination.HomeToActivities
                    )

                    FEATURE_EXPENSES -> navController.navigateToDestination(
                        RouteDestination.HomeToExpanses
                    )

                    FEATURE_DOWNLOAD -> navController.navigateToDestination(RouteDestination.HomeToDownload)
                    FEATURE_VISIT -> navController.navigateToDestination(RouteDestination.HomeToVisit)
                }
            }
        }

        composable(Routes.ACTIVITIES) {
            ActivitiesScreen(
                navController = navController,
                onClick = { trno, idMobile, locationData ->
                    navController.navigateToDestination(
                        RouteDestination.ActivityToDetail(trno, idMobile, locationData)
                    )
                },
                onClickAddActivity = {
                    navController.navigateToDestination(
                        RouteDestination.ActivityToCreateActivity(CHECK_IN)
                    )
                })
        }

        composable(Routes.DOWNLOAD) {
            DownloadScreen(navController = navController)
        }

        composable(Routes.VISIT) {
            VisitListScreen(navController = navController, onclickDetail = { id ->
                navController.navigateToDestination(RouteDestination.VisitToDetailVisit(id))
            })
        }

        composable(Routes.EXPANSES) {
            ExpansesScreen(
                navController,
                onClick = { trno ->
                    navController.navigateToDestination(
                        RouteDestination.ExpensesToExpenseDetail(trno)
                    )
                },
                onClickToAddExpanse = { navController.navigateToDestination(RouteDestination.ExpenseToCreateHeaderExpense) })
        }

        composable(
            route = Routes.DETAIL_ACTIVITIES,
            arguments = listOf(
                navArgument(TRNO_ACTIVITY) { type = NavType.StringType },
                navArgument(ID_MOBILE_ACTIVITY) { type = NavType.StringType },
                navArgument(LOCATION_DATA) { type = NavType.IntType })
        ) { backStackEntry ->
            val trno = backStackEntry.arguments?.getString(TRNO_ACTIVITY).orEmpty()
            val idMobile = backStackEntry.arguments?.getString(ID_MOBILE_ACTIVITY).orEmpty()
            val locationData = backStackEntry.arguments?.getInt(LOCATION_DATA) ?: 1

            ActivityDetailScreen(
                navController = navController,
                trno = trno,
                idMobile = idMobile,
                locationData = locationData,
                onCheckOutData = { trnoServer, dataIdMobile ->
                    navController.navigateToDestination(
                        RouteDestination.DetailActivityToCheckOutActivity(
                            trnoServer, dataIdMobile, CHECK_OUT
                        )
                    )
                })
        }

        // CHECK IN
        composable(
            Routes.CREATE_ACTIVITY, arguments = listOf(
                navArgument(TYPE_ACTIVITY) { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val typeActivity = backStackEntry.arguments?.getString(TYPE_ACTIVITY).orEmpty()

            CreateActivityScreen(navController, typeActivity = typeActivity)
        }

        // CHECK OUT
        composable(
            route = Routes.UPDATE_ACTIVITY, arguments = listOf(
                navArgument(TRNO_ACTIVITY) { type = NavType.StringType },
                navArgument(ID_MOBILE_ACTIVITY) { type = NavType.StringType },
                navArgument(TYPE_ACTIVITY) { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val trno = backStackEntry.arguments?.getString(TRNO_ACTIVITY).orEmpty()
            val idMobile = backStackEntry.arguments?.getString(ID_MOBILE_ACTIVITY).orEmpty()
            val typeActivity = backStackEntry.arguments?.getString(TYPE_ACTIVITY).orEmpty()

            CreateActivityScreen(
                navController = navController,
                trno = trno,
                typeActivity = typeActivity,
                idMobile = idMobile
            )
        }

        composable(
            Routes.DETAIL_EXPENSES,
            arguments = listOf(
                navArgument(TRNO_EXPENSE) { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val trno = backStackEntry.arguments?.getString(TRNO_EXPENSE).orEmpty()
            ExpanseDetailScreen(
                navController,
                trno = trno,
                onAddExpenseDetail = { trnoExpense ->
                    navController.navigateToDestination(
                        RouteDestination.ExpenseDetailToCreateExpenseDetail(trnoExpense)
                    )
                },
                onEditExpenseDetail = { trnoDetail, idDetail, netAmount, note ->
                    navController.navigateToDestination(
                        RouteDestination.ExpenseDetailToUpdateExpenseDetail(
                            trnoDetail, idDetail, netAmount, note
                        )
                    )
                })
        }

        composable(Routes.CREATE_EXPENSE_HEADER) {
            CreateExpenseScreen(navController)
        }

        composable(
            Routes.CREATE_EXPENSE_DETAIL, arguments = listOf(
                navArgument(TRNO_EXPENSE) { type = NavType.StringType })
        ) { navBackStackEntry ->
            val trno = navBackStackEntry.arguments?.getString(TRNO_EXPENSE).orEmpty()
            CreateAndUpdateExpenseDetailScreen(navController, trno)
        }

        composable(
            Routes.UPDATE_EXPENSE_DETAIL, arguments = listOf(
                navArgument(TRNO_EXPENSE) { type = NavType.StringType },
                navArgument(ID_EXPENSE) { type = NavType.StringType },
                navArgument(NET_AMOUNT) { type = NavType.StringType },
                navArgument(NOTE) { type = NavType.StringType },
            )
        ) { navBackStackEntry ->
            val trno = navBackStackEntry.arguments?.getString(TRNO_EXPENSE).orEmpty()
            val idExpense = navBackStackEntry.arguments?.getString(ID_EXPENSE).orEmpty()
            val netAmount = navBackStackEntry.arguments?.getString(NET_AMOUNT).orEmpty()
            val note = navBackStackEntry.arguments?.getString(NOTE).orEmpty()
            CreateAndUpdateExpenseDetailScreen(
                navController,
                trno = trno,
                dataIdDetail = idExpense,
                dataNetAmount = netAmount,
                dataNote = note
            )
        }
        composable(
            Routes.DETAIL_VISIT, arguments = listOf(
                navArgument(ID_VISIT) { type = NavType.StringType })
        ) { navBackStackEntry ->
            val trnoMobile = navBackStackEntry.arguments?.getString(ID_VISIT).orEmpty()

            VisitDetailScreen(
                idVisit = trnoMobile,
                navController = navController,
                onClickButton = { idVisit, type ->
                    navController.navigateToDestination(
                        RouteDestination.VisitDetailToCheckInVisit(idVisit, type)
                    )
                })
        }

        composable(
            Routes.CHECK_IN_VISIT, arguments = listOf(
                navArgument(ID_VISIT) { type = NavType.StringType },
                navArgument(TYPE_VISIT) { type = NavType.StringType },
            )
        ) { navBackStackEntry ->
            val trnoMobile = navBackStackEntry.arguments?.getString(ID_VISIT).orEmpty()
            val type = navBackStackEntry.arguments?.getString(TYPE_VISIT).orEmpty()

            UpdateVisitScreen(
                navController = navController, typeVisit = type, idMobile = trnoMobile
            )
        }
    }
}
