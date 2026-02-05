package com.sss.monikaapps.feature.connection.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.data.UrlModel
import com.sss.monikaapps.common.data.listUrl
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.connection.presentation.component.ConnectionCard
import com.sss.monikaapps.feature.utils.device.presentation.DeviceViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConnectionScreen(
    navController: NavController,
    isFirstTime: Boolean = false,
    onClickLogin: () -> Unit,
    viewModel: ConnectionViewModel = koinViewModel(),
    deviceViewModel: DeviceViewModel = koinViewModel(),
) {
    var selectedUrl by remember { mutableStateOf<UrlModel?>(null) }
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val imei by deviceViewModel.deviceId.observeAsState("")
    val versionApps by deviceViewModel.versionApps.observeAsState("")
    val systemOperation by deviceViewModel.systemOperation.observeAsState("")
    val state by viewModel.state.observeAsState()
    val serverUrlResult by viewModel.serverUrl.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchServerUrl()
    }

    LaunchedEffect(serverUrlResult) {
        if (serverUrlResult?.status == StatusNetwork.SUCCESS && selectedUrl == null) {
            val server = serverUrlResult?.data.orEmpty()

            val cleanServer = server
                .removePrefix("http://")
                .removePrefix("https://")

            selectedUrl = listUrl.find { it.urlValue == server }
                ?: UrlModel(
                    urlName = cleanServer,
                    urlValue = server
                )
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
                title = "Konfigurasi Koneksi",
                showLeftIcon = !isFirstTime,
                onBackClick = { navController.popBackStack() })

            Spacer(Modifier.height(Dimens.MediumMargin))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                ConnectionCard(
                    isFirstTime = isFirstTime,
                    imei = imei,
                    systemOperation = systemOperation,
                    versionApps = "V.$versionApps",
                    listUrl = listUrl,
                    selectedUrl = selectedUrl,
                    onCopy = {
                        clipboardManager.setText(AnnotatedString(imei))
                        scope.launch {
                            SnackbarManager.showSnackbar(
                                SnackbarData("Data berhasil disalin")
                            )
                        }
                    },
                    onUrlSelected = { selectedUrl = it },
                    onSave = { url ->
                        viewModel.changeServer(url)
                    },
                    onClickToLogin = {
                        SessionManager.getInstance().setFirstTimeFalse()
                        onClickLogin()
                    })
            }
        }

        // HANDLE STATE
        state?.let { result ->
            when (result.status) {
                StatusNetwork.LOADING -> CustomLoadingDialog("Loading")

                StatusNetwork.SUCCESS -> LaunchedEffect(result) {
                    SnackbarManager.showSnackbar(
                        SnackbarData(
                            "Berhasil terhubung ke server", SnackbarType.SUCCESS
                        )
                    )
                }

                StatusNetwork.ERROR -> LaunchedEffect(result) {
                    SnackbarManager.showSnackbar(
                        SnackbarData(
                            result.message ?: "Gagal terhubung", SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }
}
