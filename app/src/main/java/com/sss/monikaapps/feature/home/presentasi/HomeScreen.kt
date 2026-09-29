package com.sss.monikaapps.feature.home.presentasi

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.navigation.RouteDestination
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.developer_option.data.DeveloperModeUtils
import com.sss.monikaapps.feature.developer_option.presentation.CheckDeveloperModeBottomSheet
import com.sss.monikaapps.feature.device.presentation.DeviceViewModel
import com.sss.monikaapps.feature.home.data.model.BlockSheetType
import com.sss.monikaapps.feature.home.data.model.HomeMenuItem
import com.sss.monikaapps.feature.home.data.sealed.HomeNavEvent
import com.sss.monikaapps.feature.home.presentasi.component.HomeHeader
import com.sss.monikaapps.feature.home.presentasi.component.HomeMenuGrid
import com.sss.monikaapps.feature.login.data.response.DataItemUserLogin
import com.sss.monikaapps.feature.version_check.persentation.CheckVersionBottomSheet
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    deviceViewModel: DeviceViewModel = koinViewModel(),
    onNavigate: (RouteDestination) -> Unit,
) {
    val user by viewModel.user.collectAsState()
    val menuItems by viewModel.menuItems.collectAsStateWithLifecycle()
    val blockSheet by viewModel.blockSheetState.collectAsStateWithLifecycle()
    val downloadDate by viewModel.downloadDate.collectAsStateWithLifecycle()
    val versionApps by deviceViewModel.versionApps.observeAsState("")

    val context = LocalContext.current

    HandleEvents(viewModel, onNavigate)

    LaunchedEffect(versionApps) {
        if (versionApps.isNotEmpty()) {
            viewModel.decideBlockSheet(
                isDeveloperMode = DeveloperModeUtils.isDeveloperModeEnabled(context),
                localVersion = versionApps
            )
        }
    }

    HomeContent(
        user = user,
        menuItems = menuItems,
        onMenuClick = viewModel::onMenuClicked,
        downloadDate = downloadDate
    )

    HomeOverlays(blockSheet)
}

@Composable
private fun HomeContent(
    user: DataItemUserLogin,
    downloadDate: String,
    menuItems: List<HomeMenuItem>,
    onMenuClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
            .padding(Dimens.MediumMargin)
    ) {
        HomeHeader(
            userName = user.employeeName ?: "-",
            userRole = user.roleName ?: "-",
            nameDepo = user.namaDepo ?: "-",
            downloadDate = downloadDate
        )

        Spacer(modifier = Modifier.height(Dimens.LargeMargin))

        HomeMenuGrid(
            menuItems = menuItems,
            onMenuClick = onMenuClick
        )
    }
}

@Composable
private fun HandleEvents(
    viewModel: HomeViewModel,
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


@Composable
private fun HomeOverlays(blockSheet: BlockSheetType) {
    when (blockSheet) {
        BlockSheetType.DEVELOPER_MODE -> CheckDeveloperModeBottomSheet()
        BlockSheetType.VERSION_UPDATE -> CheckVersionBottomSheet()
        BlockSheetType.NONE -> { /* Do nothing */ }
    }
}


