package com.sss.monikaapps.feature.download.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.LightRed
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.feature.download.presentation.component.CustomTableDownload
import com.sss.monikaapps.feature.download.presentation.component.GaugeChart
import org.koin.androidx.compose.koinViewModel

@Composable
fun DownloadScreen(
    navController: NavController,
    viewModel: DownloadViewModel = koinViewModel(),
) {
    // ===== Observe Download Result =====
    val downloadResult by viewModel.downloadResult.observeAsState(Result.loading(null, 0f))

    // ===== Observe Config Table Result =====
    val configResult by viewModel.configDownloadResult
        .observeAsState(Result.loading(emptyList()))

    var allDownloaded by remember { mutableStateOf(false) }

    // ===== Progress yang stabil =====
    var displayProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(downloadResult.status, downloadResult.progress) {
        displayProgress = when (downloadResult.status) {
            StatusNetwork.SUCCESS -> 1f
            StatusNetwork.ERROR -> downloadResult.progress.coerceIn(0f, 1f)
            else -> downloadResult.progress.coerceIn(0f, 1f)
        }
    }

    // ===== Fetch table saat screen dibuka =====
    LaunchedEffect(Unit) {
        viewModel.fetchConfigDownload()
    }

    // ===== Refresh table setelah download selesai (SUCCESS / ERROR) =====
    LaunchedEffect(downloadResult.status) {
        if (
            downloadResult.status == StatusNetwork.SUCCESS ||
            downloadResult.status == StatusNetwork.ERROR
        ) {
            viewModel.fetchConfigDownload()
        }
    }

    // ===== UI =====
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        CustomTopBar(
            title = "Download Data",
            onBackClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(Dimens.MediumMargin))

        // ===== Gauge =====
        GaugeChart(
            progress = displayProgress,
            strokeWidth = 80.dp,
            modifier = Modifier.fillMaxWidth()
        )

        // ===== Status Text =====
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (downloadResult.status) {

                StatusNetwork.LOADING -> {
                    Text(
                        "${"%.0f".format(displayProgress * 100)}%",
                        style = BodyPopBold.copy(fontSize = 25.sp)
                    )
                }

                StatusNetwork.SUCCESS -> {
                    Text(
                        "100%",
                        style = BodyPopBold.copy(fontSize = 25.sp, color = Primary)
                    )

                    LaunchedEffect(downloadResult) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(
                                downloadResult.data?.message ?: "Download selesai",
                                SnackbarType.SUCCESS
                            )
                        )
                    }
                }

                StatusNetwork.ERROR -> {
                    Text(
                        "${"%.0f".format(displayProgress * 100)}%",
                        style = BodyPopBold.copy(fontSize = 25.sp, color = LightRed)
                    )

                    LaunchedEffect(downloadResult) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(
                                downloadResult.message ?: "Terjadi kesalahan",
                                SnackbarType.ERROR
                            )
                        )
                    }
                }
            }
        }

        when (configResult.status) {
            StatusNetwork.LOADING -> {
                Text("Memuat data...")
            }

            StatusNetwork.SUCCESS -> {
                val data = configResult.data ?: emptyList()

                CustomTableDownload(data = data)

                // ✅ LOGIKA INTI
                allDownloaded = data.isNotEmpty() &&
                        data.none { !it.statusTotalDownload }

                if (allDownloaded) {
                    displayProgress = 1f
                }
            }


            StatusNetwork.ERROR -> {
                Text("Gagal memuat tabel")
            }


        }

        Spacer(modifier = Modifier.weight(1f))

        CustomPrimaryButton(
            text = if (allDownloaded) "Semua Data Sudah Download" else "Download Data",
            enabled = !allDownloaded,
            onClick = { viewModel.fetchDownload() }
        )
    }
}
