package com.sss.monikaapps.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.feature.activity.ui.screen.ActivitiesScreen
import com.sss.monikaapps.feature.activity.ui.screen.ActivityDetailScreen
import com.sss.monikaapps.feature.activity.ui.screen.CreateActivityScreen
import com.sss.monikaapps.feature.expanse.ui.screen.ExpanseDetailScreen
import com.sss.monikaapps.feature.expanse.ui.screen.ExpansesScreen
import com.sss.monikaapps.feature.home.ui.screen.HomeScreen
import com.sss.monikaapps.feature.login.ui.screen.LoginScreen
import com.sss.monikaapps.utils.constanta.ArgumentsConstant.ID_MOBILE_ACTIVITY
import com.sss.monikaapps.utils.constanta.ArgumentsConstant.LOCATION_DATA
import com.sss.monikaapps.utils.constanta.ArgumentsConstant.TRNO_ACTIVITY
import com.sss.monikaapps.utils.constanta.ArgumentsConstant.TRNO_EXPANSE
import com.sss.monikaapps.utils.constanta.ArgumentsConstant.TYPE_ACTIVITY
import com.sss.monikaapps.utils.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.utils.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.utils.constanta.HomeFeatureConstant


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
                    HomeFeatureConstant.FEATURE_ACTIVITIES -> navController.navigateToDestination(
                        RouteDestination.HomeToActivities
                    )

                    HomeFeatureConstant.FEATURE_EXPANSES -> navController.navigateToDestination(
                        RouteDestination.HomeToExpanses
                    )
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

        composable(Routes.EXPANSES) {
            ExpansesScreen(navController) { trno ->
                navController.navigateToDestination(RouteDestination.ExpansesToDetailExpanse(trno))
            }
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
            Routes.DETAIL_EXPANSES, arguments = listOf(
            navArgument(TRNO_EXPANSE) { type = NavType.StringType })) { backStackEntry ->
            val trno = backStackEntry.arguments?.getString(TRNO_EXPANSE).orEmpty()
            ExpanseDetailScreen(navController, trno)
        }
    }
}
