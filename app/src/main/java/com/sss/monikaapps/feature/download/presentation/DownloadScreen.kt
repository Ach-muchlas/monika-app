package com.sss.monikaapps.feature.download.presentation

import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDate
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray
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
    val downloadResult by viewModel.downloadResult.observeAsState(Result.success(null))

    // ===== Observe Config Table Result =====
    val configResult by viewModel.configDownloadResult
        .observeAsState(Result.loading(emptyList()))

    var allDownloaded by remember { mutableStateOf(false) }


    var displayProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(configResult.status) {
        if (configResult.status == StatusNetwork.SUCCESS) {
            val data = configResult.data ?: emptyList()
            if (data.isEmpty()) {
                viewModel.initConfigIfEmpty()
            }
        }
    }

    LaunchedEffect(downloadResult.status, downloadResult.progress) {
        displayProgress = when (downloadResult.status) {
            StatusNetwork.SUCCESS -> 1f
            StatusNetwork.ERROR -> downloadResult.progress.coerceIn(0f, 1f)
            else -> downloadResult.progress.coerceIn(0f, 1f)
        }
    }

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

    LaunchedEffect(Unit) {
        viewModel.messageEvent.collect { message ->
            SnackbarManager.showSnackbar(
                SnackbarData(message, SnackbarType.SUCCESS)
            )
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
                val percentageText = "${"%.0f".format(displayProgress * 100)}%"

                when (downloadResult.status) {
                    StatusNetwork.LOADING -> {
                        Text(
                            percentageText,
                            style = BodyPopBold.copy(fontSize = 25.sp)
                        )
                    }

                    StatusNetwork.SUCCESS -> {
                        val colorText = if (allDownloaded) Primary else Gray

                        Text(
                            if (downloadResult.data != null) "100%" else percentageText,
                            style = BodyPopBold.copy(fontSize = 25.sp, color = colorText)
                        )

                        LaunchedEffect(downloadResult) {
                            if (downloadResult.data != null) {
                                SnackbarManager.showSnackbar(
                                    SnackbarData(
                                        "Download semua data selesai",
                                        SnackbarType.SUCCESS
                                    )
                                )
                            }
                        }
                    }

                    StatusNetwork.ERROR -> {
                        Text(
                            percentageText,
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
                val currentDate = getCurrentDate()
                val lastDownloadDate = data.firstOrNull()?.createAd ?: ""

                CustomTableDownload(data)

                val isSameDay = lastDownloadDate == currentDate
                val isAllStatusSuccess = data.isNotEmpty() && data.none { !it.statusTotalDownload }

                allDownloaded = isSameDay && isAllStatusSuccess

                if (downloadResult.status != StatusNetwork.LOADING) {
                    if (!isSameDay) {
                        displayProgress = 0f
                    } else {
                        val totalTabel = data.size
                        val tabelSukses = data.count { it.statusTotalDownload }

                        displayProgress = if (totalTabel > 0) {
                            tabelSukses.toFloat() / totalTabel.toFloat()
                        } else 0f
                    }
                }
            }

            StatusNetwork.ERROR -> {
                Text("Gagal memuat tabel")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CustomPrimaryButton(
            text = if (allDownloaded) "Semua Data Sudah Download" else stringResource(R.string.text_download_data),
            enabled = !allDownloaded,
            onClick = { viewModel.checkAndFetchDownload() }
        )
    }
}
