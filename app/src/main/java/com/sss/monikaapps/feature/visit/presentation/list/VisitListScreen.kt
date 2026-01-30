package com.sss.monikaapps.feature.visit.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.visit.presentation.list.component.CardListVisit
import org.koin.androidx.compose.koinViewModel

@Composable
fun VisitListScreen(
    navController: NavController,
    viewModel: VisitViewModel = koinViewModel(),
    onclickDetail: (idVisit: String) -> Unit,
) {

    val result by viewModel.visitResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchVisit()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
            .padding(Dimens.MediumMargin)
    ) {
        CustomTopBar(
            title = stringResource(R.string.list_visit),
            showRightIcon = true,
            iconRight = R.drawable.icon_sync2,
            iconSize = 30,
            onBackClick = { navController.popBackStack() },
            onRightIconClick = {}
        )

        Spacer(modifier = Modifier.height(Dimens.MediumMargin))

//        CustomSearch(
//            query = searchQuery, onQueryChange = { searchQuery = it })

        Spacer(modifier = Modifier.height(Dimens.MediumMargin))

        when (result?.status) {
            StatusNetwork.LOADING -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
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
                            CardListVisit(
                                data = data,
                            ) { onclickDetail(data.id) }
                            Spacer(modifier = Modifier.height(4.dp))
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
            }

            else -> Unit
        }
    }

}