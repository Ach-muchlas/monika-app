package com.sss.monikaapps.feature.expense.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomFloatingActionButton
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.formatter.FormatterCurrency.formatCurrency
import com.sss.monikaapps.common.formatter.FormatterDate.formatDateToIndoDisplay
import com.sss.monikaapps.common.mapper.MapperExpanse.mapperStatusExpanse
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.expense.presentation.detail.component.CardDetailItemExpense
import com.sss.monikaapps.feature.expense.presentation.detail.component.CardHeaderExpanseDetail
import com.sss.monikaapps.feature.expense.presentation.detail.handler.ExpanseDetailActionHandler
import com.sss.monikaapps.feature.expense.presentation.detail.handler.ExpanseDetailResultHandler
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpanseDetailScreen(
    navController: NavController,
    trno: String,
    viewModel: ExpenseDetailViewModel = koinViewModel(),
    onAddExpenseDetail: (trno: String) -> Unit,
    onEditExpenseDetail: (trno: String, idExpense: String, netAmount: String, note: String, initKm: String, finalKm: String) -> Unit,
) {
    var status = "0"
    var dialogAction by remember { mutableStateOf<ExpenseDetailDialogAction?>(null) }

    val result by viewModel.detailExpanseResult.observeAsState()
    val deleteExpenseDetailResult by viewModel.deleteDetailExpenseResult.observeAsState()
    val deleteExpenseHeaderResult by viewModel.deleteHeaderExpenseResult.observeAsState()
    val submitExpenseResult by viewModel.submitExpenseResult.observeAsState()
    val unSubmitExpenseResult by viewModel.unSubmitExpenseResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDetailExpanse(trno)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .background(BackgroundLayout)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(Dimens.MediumMargin)
            ) {
                when (result?.status) {
                    StatusNetwork.LOADING -> {}
                    StatusNetwork.SUCCESS -> {
                        val header = result?.data?.data?.header
                        val detail = result?.data?.data?.detail
                        status = result?.data?.data?.header?.isStatus.toString()

                        CustomTopBar(
                            title = stringResource(R.string.text_detail_expanse),
                            showRightIcon = header?.isStatus == "0",
                            iconRight = R.drawable.icon_trash,
                            iconSize = 24,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onRightIconClick = {
                                dialogAction = ExpenseDetailDialogAction.DeleteHeader(trno)
                            })

                        Spacer(modifier = Modifier.height(Dimens.MediumMargin))

                        CardHeaderExpanseDetail(
                            employeeName = header?.employeeName.toString(),
                            netAmount = formatCurrency(amount = header?.netAmount?.toLong() ?: 0),
                            date = formatDateToIndoDisplay(header?.date.toString()),
                            status = mapperStatusExpanse(header?.isStatus),
                            note = header?.note.orEmpty().ifBlank { "-" },
                            createdAt = header?.createdAt.toString()
                        )

                        Spacer(modifier = Modifier.height(Dimens.LargeMargin))

                        detail?.forEach { data ->
                            CardDetailItemExpense(
                                dataItem = data,
                                status = status,
                                lisPhoto = data.photo,
                                onClickEdited = { trnoDetail, idDetail, netAmount, note, initKm, finalKm ->
                                    dialogAction = ExpenseDetailDialogAction.EditDetail(
                                        trnoDetail, idDetail, netAmount, note, initKm, finalKm
                                    )

                                },
                                onClickDeleted = { trnoDetail, idDetail ->
                                    dialogAction =
                                        ExpenseDetailDialogAction.DeleteDetail(trnoDetail, idDetail)
                                })

                            Spacer(modifier = Modifier.height(Dimens.LargeMargin))
                        }
                    }

                    StatusNetwork.ERROR -> {
                        LaunchedEffect(result?.message) {
                            SnackbarManager.showSnackbar(
                                SnackbarData(
                                    result?.message ?: "Gagal mengambil pengeluaran",
                                    SnackbarType.ERROR
                                )
                            )
                        }
                    }

                    else -> Unit
                }

            }
        }


        if (status == "0" || status == "4") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                CustomPrimaryButton(
                    text = if (status == "4") "Batal Diajukan" else "Diajukan",
                    modifier = Modifier.weight(1f)
                ) {
                    dialogAction =
                        if (status == "0") ExpenseDetailDialogAction.Submit(trno = trno) else ExpenseDetailDialogAction.UnSubmit(
                            trno = trno
                        )
                }

                // FAB hanya muncul saat status = 0
                if (status == "0") {
                    CustomFloatingActionButton(
                        modifier = Modifier.height(54.dp),
                        onClick = { onAddExpenseDetail(trno) }
                    )
                }
            }
        }

        ExpanseDetailActionHandler(
            action = dialogAction,
            viewModel = viewModel,
            onClearAction = { dialogAction = null },
            onEditAction = onEditExpenseDetail
        )


        ExpanseDetailResultHandler(
            submitResult = submitExpenseResult,
            unSubmitResult = unSubmitExpenseResult,
            deleteHeaderResult = deleteExpenseHeaderResult,
            deleteDetailResult = deleteExpenseDetailResult,
            trno = trno,
            navController = navController,
            viewModel = viewModel
        )
    }
}