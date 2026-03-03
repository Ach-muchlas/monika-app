package com.sss.monikaapps.feature.activity.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.sss.monikaapps.common.component.CustomCardListHeader
import com.sss.monikaapps.common.component.CustomFloatingActionButton
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomLoadingView
import com.sss.monikaapps.common.component.CustomNotFoundAnimation
import com.sss.monikaapps.common.component.CustomSearch
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.model.dataStatusActivities
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActivitiesScreen(
    navController: NavController,
    viewModel: ActivitiesViewModel = koinViewModel(),
    onClick: (trno: String, idMobile: String) -> Unit,
    onClickAddActivity: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    val result by viewModel.activitiesResult.observeAsState()
    val resultSyncManual by viewModel.syncManualResult.observeAsState()

    val isSyncing = resultSyncManual?.status == StatusNetwork.LOADING

    LaunchedEffect(Unit) {
        viewModel.fetchActivities()
    }

    LaunchedEffect(resultSyncManual?.status) {
        when (resultSyncManual?.status) {
            StatusNetwork.SUCCESS -> {
                SnackbarManager.showSnackbar(
                    SnackbarData(
                        resultSyncManual?.data.toString(),
                        SnackbarType.SUCCESS
                    )
                )
                viewModel.fetchActivities()
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

        // ===== MAIN CONTENT =====
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.MediumMargin)
        ) {
            CustomTopBar(
                title = stringResource(R.string.list_activity),
                showRightIcon = true,
                iconRight = R.drawable.icon_sync,
                iconSize = 30,
                onBackClick = { navController.popBackStack() },
                onRightIconClick = {
                    if (!isSyncing) {
                        viewModel.syncManual()
                    }
                }
            )

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            CustomSearch(
                query = searchQuery, onQueryChange = { searchQuery = it })



            when (result?.status) {
                StatusNetwork.LOADING -> {
                    CustomLoadingView()
                }

                StatusNetwork.SUCCESS -> {
                    val activities = result?.data.orEmpty()

                    if (activities.isEmpty()) {
                        CustomNotFoundAnimation()
                    } else {
                        Spacer(modifier = Modifier.height(Dimens.MediumMargin))
                        LazyColumn {
                            items(activities) { data ->
                                val status = dataStatusActivities.firstOrNull {
                                    it.id == data.isSync
                                }
                                CustomCardListHeader(
                                    title = data.title.orEmpty(),
                                    subtitle = data.description.orEmpty(),
                                    status = status,
                                    onClick = {
                                        onClick(
                                            data.trno.toString(),
                                            data.trnoMobile.toString(),
                                        )
                                    })
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }

                StatusNetwork.ERROR -> {
                    LaunchedEffect(result?.message) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(
                                result?.message ?: "Gagal mengambil aktivitas", SnackbarType.ERROR
                            )
                        )
                    }
//                    CustomNotFoundAnimation()
                }

                else -> Unit
            }
        }

        // ===== FLOATING ACTION BUTTON =====
        CustomFloatingActionButton(
            onClick = onClickAddActivity, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp, bottom = 20.dp
                )
        )

        if (isSyncing) {
            CustomLoadingDialog(message = "Menyinkronkan data...")
        }
    }
}
