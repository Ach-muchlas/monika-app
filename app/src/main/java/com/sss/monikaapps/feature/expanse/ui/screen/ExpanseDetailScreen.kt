package com.sss.monikaapps.feature.expanse.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.feature.expanse.presentation.ExpansesViewModel
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.formatter.FormatterCurrency.formatCurrency
import com.sss.monikaapps.feature.expanse.ui.component.CardDetailItemExpanse
import com.sss.monikaapps.feature.expanse.ui.component.CardHeaderExpanseDetail
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.formatter.FormatterDate.formatDateToIndoDisplay
import com.sss.monikaapps.common.mapper.MapperExpanse.mapperStatusExpanse
import com.sss.monikaapps.common.snackbar.SnackbarManager
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpanseDetailScreen(
    navController: NavController,
    trno: String,
    viewModel: ExpansesViewModel = koinViewModel(),
) {

    val result by viewModel.detailExpanseResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDetailExpanse(trno)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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

                    CustomTopBar(
                        title = stringResource(R.string.text_detail_expanse),
                        showRightIcon = header?.isStatus == "0",
                        iconRight = R.drawable.icon_trash,
                        iconSize = 24,
                        onBackClick = {
                            navController.popBackStack()
                        })

                    Spacer(modifier = Modifier.height(Dimens.MediumMargin))

                    CardHeaderExpanseDetail(
                        employeeName = header?.employeeId.toString(),
                        netAmount = formatCurrency(amount = header?.netAmount?.toLong() ?: 0),
                        date = formatDateToIndoDisplay(header?.date.toString()),
                        status = mapperStatusExpanse(header?.isStatus),
                        note = header?.note.orEmpty().ifBlank { "-" },
                        createdAt = header?.createdAt.toString()
                    )

                    Spacer(modifier = Modifier.height(Dimens.LargeMargin))

                    detail?.forEach { data ->
                        CardDetailItemExpanse(
                            typeExpanse = data.name.toString(),
                            isRequiredKm = data.initialKilometer != "0",
                            netAmount = data.netAmountDetail.toString(),
                            initialKilometer = data.initialKilometer ?: "0",
                            finalKilometer = data.finalKilometer ?: "0",
                            lisPhoto = data.photo
                        )

                        Spacer(modifier = Modifier.height(Dimens.LargeMargin))
                    }
                }

                StatusNetwork.ERROR -> {
                    LaunchedEffect(result?.message) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(
                                result?.message ?: "Gagal mengambil pengeluaran", SnackbarType.ERROR
                            )
                        )
                    }
                }

                else -> Unit
            }

        }
    }

}