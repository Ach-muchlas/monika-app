package com.sss.monikaapps.common.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sss.monikaapps.common.constanta.ArgumentsConstant.CUSTOMER_ID
import com.sss.monikaapps.common.constanta.ArgumentsConstant.FINAL_KM
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_EXPENSE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_INVOICE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_MOBILE_ACTIVITY
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_VISIT
import com.sss.monikaapps.common.constanta.ArgumentsConstant.INIT_KM
import com.sss.monikaapps.common.constanta.ArgumentsConstant.NET_AMOUNT
import com.sss.monikaapps.common.constanta.ArgumentsConstant.NOMOR_NOTA
import com.sss.monikaapps.common.constanta.ArgumentsConstant.NOTE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TRNO_ACTIVITY
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TRNO_EXPENSE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TYPE_ACTIVITY
import com.sss.monikaapps.common.constanta.ArgumentsConstant.TYPE_VISIT
import com.sss.monikaapps.common.constanta.ArgumentsConstant.URL_PHOTO
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_EXPENSES
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.feature.activity.presentation.ActivitiesScreen
import com.sss.monikaapps.feature.activity.presentation.ActivityDetailScreen
import com.sss.monikaapps.feature.activity.presentation.CreateActivityScreen
import com.sss.monikaapps.feature.connection.presentation.ConnectionScreen
import com.sss.monikaapps.feature.download.presentation.DownloadScreen
import com.sss.monikaapps.feature.expense.presentation.create.CreateAndUpdateExpenseDetailScreen
import com.sss.monikaapps.feature.expense.presentation.create.CreateExpenseScreen
import com.sss.monikaapps.feature.expense.presentation.detail.ExpanseDetailScreen
import com.sss.monikaapps.feature.expense.presentation.list.ExpansesScreen
import com.sss.monikaapps.feature.home.presentasi.HomeScreen
import com.sss.monikaapps.feature.invoice.presentation.detail.InvoiceDetailScreen
import com.sss.monikaapps.feature.invoice.presentation.generate_pdf.GeneratePdfScreen
import com.sss.monikaapps.feature.invoice.presentation.list.InvoiceListScreen
import com.sss.monikaapps.feature.invoice.presentation.payment.PaymentInvoiceScreen
import com.sss.monikaapps.feature.login.presentation.LoginScreen
import com.sss.monikaapps.feature.mastering.presentation.MasterScreen
import com.sss.monikaapps.feature.mastering.presentation.MasteringCategoryExpenseScreen
import com.sss.monikaapps.feature.photo.PhotoDetailScreen
import com.sss.monikaapps.feature.result_download.presentation.ResultDownloadScreen
import com.sss.monikaapps.feature.setting.presentation.SettingScreen
import com.sss.monikaapps.feature.update_data.presentation.UpdateDataScreen
import com.sss.monikaapps.feature.update_data_invoice.presentation.UpdateDataInvoiceScreen
import com.sss.monikaapps.feature.visit.presentation.detail.VisitDetailScreen
import com.sss.monikaapps.feature.visit.presentation.list.VisitListScreen
import com.sss.monikaapps.feature.visit.presentation.update.UpdateVisitScreen


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    val sessionManager = remember {
        SessionManager.getInstance()
    }

    val startDestination = remember {
        if (sessionManager.isFirstTime()) {
            Routes.CONNECTION
        } else {
            if (sessionManager.isUserLogin()) Routes.HOME else Routes.LOGIN
        }
    }

    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onSettingConnection = { navController.navigateToDestination(RouteDestination.LoginToConnection) },
                onSuccessLoginSuccess = {
                    navController.navigateToDestination(
                        RouteDestination.LoginToHome
                    )
                })
        }

        composable(Routes.CONNECTION) {
            ConnectionScreen(
                navController = navController, isFirstTime = true, onClickLogin = {
                    navController.navigateToDestination(RouteDestination.ConnectionToLogin)
                })
        }

        composable(Routes.HOME) {
            HomeScreen(
                onNavigate = { destination ->
                    navController.navigateToDestination(destination)
                })
        }


        composable(Routes.MASTER_DATA) {
            MasterScreen(navController) { idMenu ->
                when (idMenu) {
                    FEATURE_EXPENSES -> navController.navigateToDestination(RouteDestination.MasterDataToMasterDataExpense)
                }
            }
        }

        composable(Routes.UPDATE_DATA) {
            UpdateDataScreen(
                navController,
                onNavigate = { destination -> navController.navigateToDestination(destination) })
        }
        composable(Routes.UPDATE_DATA_INVOICE) {
            UpdateDataInvoiceScreen(navController)
        }

        composable(Routes.INVOICE) {
            InvoiceListScreen(
                navController,
                onItemClick = { customerId ->
                    navController.navigateToDestination(
                        RouteDestination.ListInvoiceToDetailInvoice(
                            customerId
                        )
                    )
                }, onDownload = {
                    navController.navigateToDestination(RouteDestination.InvoiceToGeneratePdf)
                }
            )
        }

        composable(Routes.MASTER_DATA_EXPENSE) {
            MasteringCategoryExpenseScreen(navController)
        }

        composable(Routes.ACTIVITIES) {
            ActivitiesScreen(navController = navController, onClick = { trno, idMobile ->
                navController.navigateToDestination(
                    RouteDestination.ActivityToDetail(trno, idMobile)
                )
            }, onClickAddActivity = {
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
            route = Routes.DETAIL_ACTIVITIES, arguments = listOf(
                navArgument(TRNO_ACTIVITY) { type = NavType.StringType },
                navArgument(ID_MOBILE_ACTIVITY) { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val trno = backStackEntry.arguments?.getString(TRNO_ACTIVITY).orEmpty()
            val idMobile = backStackEntry.arguments?.getString(ID_MOBILE_ACTIVITY).orEmpty()

            ActivityDetailScreen(
                navController = navController,
                trno = trno,
                idMobile = idMobile,
                clickDetailPhoto = { url ->
                    navController.navigateToDestination(
                        RouteDestination.ToDetailPhoto(
                            url
                        )
                    )
                },
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
            Routes.DETAIL_EXPENSES, arguments = listOf(
                navArgument(TRNO_EXPENSE) { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val trno = backStackEntry.arguments?.getString(TRNO_EXPENSE).orEmpty()
            ExpanseDetailScreen(navController, trno = trno, onAddExpenseDetail = { trnoExpense ->
                navController.navigateToDestination(
                    RouteDestination.ExpenseDetailToCreateExpenseDetail(trnoExpense)
                )
            }, onEditExpenseDetail = { trnoDetail, idDetail, netAmount, note, initialKm, finalKm ->
                navController.navigateToDestination(
                    RouteDestination.ExpenseDetailToUpdateExpenseDetail(
                        trnoDetail, idDetail, netAmount, note, initialKm, finalKm
                    )
                )
            }, clickDetailPhoto = { url ->
                navController.navigateToDestination(
                    RouteDestination.ToDetailPhoto(url)
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
                navArgument(INIT_KM) { type = NavType.StringType },
                navArgument(FINAL_KM) { type = NavType.StringType },
            )
        ) { navBackStackEntry ->
            val trno = navBackStackEntry.arguments?.getString(TRNO_EXPENSE).orEmpty()
            val idExpense = navBackStackEntry.arguments?.getString(ID_EXPENSE).orEmpty()
            val netAmount = navBackStackEntry.arguments?.getString(NET_AMOUNT).orEmpty()
            val note = navBackStackEntry.arguments?.getString(NOTE).orEmpty()
            val initialKm = navBackStackEntry.arguments?.getString(INIT_KM) ?: "0"
            val finalKm = navBackStackEntry.arguments?.getString(FINAL_KM) ?: "0"

            CreateAndUpdateExpenseDetailScreen(
                navController,
                trno = trno,
                dataInitialKm = initialKm,
                dataFinalKm = finalKm,
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
                clickDetailPhoto = { url ->
                    navController.navigateToDestination(RouteDestination.ToDetailPhoto(url))
                },
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

        composable(Routes.SETTING) {
            SettingScreen(
                navController = navController,
                onClickConnection = { navController.navigateToDestination(RouteDestination.SettingToConnection) },
                onClickDownload = { navController.navigateToDestination(RouteDestination.SettingToResultDownload) },
                onClickLogout = { navController.navigateToDestination(RouteDestination.SettingToLogin) })
        }

        composable(Routes.CONNECTION_SETTING) {
            ConnectionScreen(navController, isFirstTime = false, onClickLogin = {
                navController.navigateToDestination(RouteDestination.LoginToConnection)
            })
        }

        composable(Routes.RESULT_DOWNLOAD) {
            ResultDownloadScreen(navController)
        }

        composable(
            Routes.DETAIL_PHOTO, arguments = listOf(
                navArgument(URL_PHOTO) { type = NavType.StringType })
        ) { navBackStackEntry ->
            val urlPhoto = navBackStackEntry.arguments?.getString(URL_PHOTO).orEmpty()
            PhotoDetailScreen(urlPhoto, navController)
        }

        composable(
            Routes.DETAIL_INVOICE, arguments = listOf(
                navArgument(CUSTOMER_ID) { type = NavType.StringType })
        ) { _ ->
            InvoiceDetailScreen(
                navController,
                onClickPayment = { idInvoice, nomorNota, customerId ->
                    navController.navigateToDestination(
                        RouteDestination.DetailInvoiceToPaymentInvoice(
                            idInvoice, nomorNota, customerId
                        )
                    )
                },
                clickDetailPhoto = { url ->
                    navController.navigateToDestination(
                        RouteDestination.ToDetailPhoto(url)
                    )
                }
            )
        }

        composable(
            Routes.PAYMENT_INVOICE, arguments = listOf(
                navArgument(NOMOR_NOTA) { type = NavType.StringType },
                navArgument(ID_INVOICE) { type = NavType.StringType },
                navArgument(CUSTOMER_ID) { type = NavType.StringType }
            )) { _ ->
            PaymentInvoiceScreen(navController)
        }

        composable(
            Routes.GENERATE_PDF
        ) {
            GeneratePdfScreen(navController)
        }
    }
}
