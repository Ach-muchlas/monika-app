package com.sss.monikaapps.feature.update_data.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.navigation.RouteDestination
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.home.data.sealed.HomeNavEvent
import com.sss.monikaapps.feature.home.presentasi.HomeViewModel
import com.sss.monikaapps.feature.home.presentasi.component.HomeMenuGrid
import org.koin.androidx.compose.koinViewModel

@Composable
fun UpdateDataScreen(
    navController: NavController,
    viewModel: UpdateDataViewModel = koinViewModel(),
    onNavigate: (RouteDestination) -> Unit,
) {

    val menuItems by viewModel.menuItems.collectAsStateWithLifecycle()
    HandleEvents(viewModel, onNavigate)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
            .padding(Dimens.MediumMargin)
    ) {
        CustomTopBar(
            title = stringResource(R.string.text_feature_update_data),
            onBackClick = { navController.popBackStack() },
        )

        Spacer(modifier = Modifier.height(Dimens.MediumMargin))

        HomeMenuGrid(
            menuItems = menuItems,
            onMenuClick = viewModel::onMenuClicked
        )
    }
}


@Composable
private fun HandleEvents(
    viewModel: UpdateDataViewModel,
    onNavigate: (RouteDestination) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is HomeNavEvent.Navigate -> onNavigate(event.destination)
                is HomeNavEvent.Blocked -> {
                    SnackbarManager.showSnackbar(
                        SnackbarData(event.message, SnackbarType.ERROR)
                    )
                }
            }
        }
    }
}
