package com.sss.monikaapps.feature.visit.presentation.list

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomFilterChip
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomSearch
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.model.dataStatusVisit
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.visit.presentation.list.component.CardListVisit
import com.sss.monikaapps.feature.visit.presentation.update.UpdateVisitViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun VisitListScreen(
    navController: NavController,
    viewModel: VisitViewModel = koinViewModel(),
    updateViewModel: UpdateVisitViewModel = koinViewModel(),
    onclickDetail: (idVisit: String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableIntStateOf(7) }

    val result by viewModel.visitResult.observeAsState()
    val resultSyncManual by updateViewModel.syncManualResult.observeAsState()

    val isSyncing = resultSyncManual?.status == StatusNetwork.LOADING

    // Fetch data pertama kali
    LaunchedEffect(Unit) {
        viewModel.fetchVisit(searchQuery, selectedStatus)
    }

    // Handle hasil sinkronisasi
    LaunchedEffect(resultSyncManual?.status) {
        when (resultSyncManual?.status) {
            StatusNetwork.SUCCESS -> {
                SnackbarManager.showSnackbar(
                    SnackbarData(
                        resultSyncManual?.data.toString(),
                        SnackbarType.SUCCESS
                    )
                )
                viewModel.fetchVisit(searchQuery, selectedStatus)
                updateViewModel.clearSyncState()
            }

            StatusNetwork.ERROR -> {
                SnackbarManager.showSnackbar(
                    SnackbarData(
                        resultSyncManual?.message ?: "Gagal sinkronisasi",
                        SnackbarType.ERROR
                    )
                )
                updateViewModel.clearSyncState()
            }

            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
            .padding(Dimens.MediumMargin)
    ) {
        // Top Bar
        CustomTopBar(
            title = stringResource(R.string.list_visit),
            showRightIcon = true,
            iconRight = R.drawable.icon_sync,
            iconSize = 30,
            onBackClick = { navController.popBackStack() },
            onRightIconClick = {
                if (!isSyncing) updateViewModel.syncManual()
            }
        )

        Spacer(modifier = Modifier.height(Dimens.MediumMargin))

        // Search
        CustomSearch(
            query = searchQuery,
            onQueryChange = {
                searchQuery = it
                viewModel.setKeyword(it)
            }
        )

        Spacer(modifier = Modifier.height(Dimens.SmallMargin))


        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens.ExtraExtraSmallCornerRadius)
        ) {
            items(dataStatusVisit) { item ->

                CustomFilterChip(
                    text = item.title,
                    selected = selectedStatus.toString() == item.id,
                    onClick = {
                        selectedStatus = item.id.toInt()
                        viewModel.setStatus(selectedStatus)
                    }
                )

            }
        }

        Spacer(modifier = Modifier.height(Dimens.MediumMargin))

        // List Visit
        when (result?.status) {
            StatusNetwork.LOADING -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            StatusNetwork.SUCCESS -> {
                val visit = result?.data.orEmpty()
                if (visit.isEmpty()) {
                    Text("Tidak ada data kunjungan", color = Color.Gray)
                } else {
                    LazyColumn {
                        items(visit) { data ->
                            CardListVisit(data = data) {
                                onclickDetail(data.id)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }

            StatusNetwork.ERROR -> {
                LaunchedEffect(result?.message) {
                    SnackbarManager.showSnackbar(
                        SnackbarData(
                            result?.message ?: "Gagal mengambil aktivitas",
                            SnackbarType.ERROR
                        )
                    )
                }
            }

            else -> Unit
        }
    }

    if (isSyncing) {
        CustomLoadingDialog(message = "Menyinkronkan data...")
    }
}
