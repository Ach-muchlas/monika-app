package com.sss.monikaapps.feature.invoice.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomFilterChip
import com.sss.monikaapps.common.component.CustomFloatingActionButton
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomLoadingView
import com.sss.monikaapps.common.component.CustomNotFoundAnimation
import com.sss.monikaapps.common.component.CustomSearch
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.model.dataStatusInvoice
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.invoice.presentation.list.component.CardListInvoice
import org.koin.androidx.compose.koinViewModel

@Composable
fun InvoiceListScreen(
    navController: NavController,
    viewModel: InvoiceListViewModel = koinViewModel(),
    onItemClick: (customerId: String) -> Unit,
    onDownload: () -> Unit,
) {

    val context = LocalContext.current


    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val result by viewModel.customerInvoice.collectAsStateWithLifecycle()

    val resultSyncManual by viewModel.syncManualResult.observeAsState()

    val isSyncing = resultSyncManual?.status == StatusNetwork.LOADING
    val selectedStatus by viewModel.selectedStatus.collectAsStateWithLifecycle()

    LaunchedEffect(resultSyncManual?.status) {
        when (resultSyncManual?.status) {
            StatusNetwork.SUCCESS -> {
                SnackbarManager.showSnackbar(
                    SnackbarData(
                        resultSyncManual?.data.toString(),
                        SnackbarType.SUCCESS
                    )
                )
                viewModel.customerInvoice
                viewModel.clearSyncState()
            }

            StatusNetwork.ERROR -> {
                SnackbarManager.showSnackbar(
                    SnackbarData(
                        resultSyncManual?.message ?: "Gagal sinkronisasi",
                        SnackbarType.ERROR
                    )
                )
                viewModel.clearSyncState()
            }

            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.MediumMargin)
        ) {

            CustomTopBar(
                title = stringResource(R.string.list_invoice),
                showRightIcon = true,
                iconRight = R.drawable.icon_sync,
                iconSize = 30,
                onBackClick = { navController.popBackStack() },
                onRightIconClick = { if (!isSyncing) viewModel.syncManual() }
            )

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            CustomSearch(
                query = searchQuery, onQueryChange = viewModel::onSearchQueryChanged
            )

            Spacer(modifier = Modifier.height(Dimens.SmallMargin))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.ExtraExtraSmallCornerRadius)
            ) {
                items(dataStatusInvoice) { item ->
                    CustomFilterChip(
                        text = item.title,
                        selected = selectedStatus.toString() == item.id,
                        onClick = {
                            viewModel.onStatusChanged(item.id.toInt())
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            when (result.status) {
                StatusNetwork.LOADING -> {
                    CustomLoadingView()
                }

                StatusNetwork.SUCCESS -> {
                    val invoices = result.data.orEmpty()
                    if (invoices.isEmpty()) {
                        CustomNotFoundAnimation()
                    } else {
                        LazyColumn {
                            items(invoices) { item ->
                                CardListInvoice(data = item) {
                                    onItemClick.invoke(item.customerId)
                                }
                            }
                        }
                    }
                }

                StatusNetwork.ERROR -> {
                    CustomNotFoundAnimation()
                }
            }
        }

        CustomFloatingActionButton(
            imageVector = Icons.Default.FileDownload,
            onClick = onDownload,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp, bottom = 20.dp
                )
        )


        if (isSyncing) {
            CustomLoadingDialog(message = "Menyinkronkan data")
        }
    }
}