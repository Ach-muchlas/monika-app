package com.sss.monikaapps.feature.update_data_invoice.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun UpdateDataInvoiceScreen(
    navController: NavController,
    viewModel: UpdateDataInvoiceViewModel = koinViewModel(),
) {
    // ===== Observe Download Result =====
    val updateDataResult by viewModel.updateDataResult.observeAsState(Result.success(null))

    // ===== Observe Config Table Result =====
    val configResult by viewModel.configDownloadResult
        .observeAsState(Result.loading(emptyList()))

    var allDownloaded by remember { mutableStateOf(false) }

    var displayProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(updateDataResult.status, updateDataResult.progress) {
        displayProgress = when (updateDataResult.status) {
            StatusNetwork.SUCCESS -> 1f
            StatusNetwork.ERROR -> updateDataResult.progress.coerceIn(0f, 1f)
            else -> updateDataResult.progress.coerceIn(0f, 1f)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchConfigUpdated()
    }

    // ===== Refresh table setelah download selesai (SUCCESS / ERROR) =====
    LaunchedEffect(updateDataResult.status) {
        if (
            updateDataResult.status == StatusNetwork.SUCCESS ||
            updateDataResult.status == StatusNetwork.ERROR
        ) {
            viewModel.fetchConfigUpdated()
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
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        CustomTopBar(
            title = "Update Data Tagihan",
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

            when (updateDataResult.status) {
                StatusNetwork.LOADING -> {
                    Text(
                        percentageText,
                        style = BodyPopBold.copy(fontSize = 25.sp)
                    )
                }

                StatusNetwork.SUCCESS -> {
                    val colorText = if (allDownloaded) Primary else Gray

                    Text(
                        if (updateDataResult.data != null) "100%" else percentageText,
                        style = BodyPopBold.copy(fontSize = 25.sp, color = colorText)
                    )
                    displayProgress = 1f

                    LaunchedEffect(updateDataResult) {
                        if (updateDataResult.data != null) {
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

                    LaunchedEffect(updateDataResult) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(
                                updateDataResult.message ?: "Terjadi kesalahan",
                                SnackbarType.ERROR
                            )
                        )
                    }
                }
            }
        }

        when (configResult.status) {
            StatusNetwork.LOADING -> {
                Text(stringResource(R.string.text_loading))
            }

            StatusNetwork.SUCCESS -> {
                val data = configResult.data ?: emptyList()
                CustomTableDownload(data)
            }

            StatusNetwork.ERROR -> {
                Text(stringResource(R.string.text_error_loading_table))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        CustomPrimaryButton(
            text = stringResource(R.string.text_update_data),
            onClick = { viewModel.fetchUpdatedDataInvoice() }
        )
    }
}
