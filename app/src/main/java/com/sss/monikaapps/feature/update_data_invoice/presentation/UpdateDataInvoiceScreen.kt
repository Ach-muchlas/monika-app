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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sss.monikaapps.R
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
import com.sss.monikaapps.common.theme.Gray
import com.sss.monikaapps.common.theme.LightRed
import com.sss.monikaapps.common.theme.PieGray
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.feature.download.presentation.component.GaugeChart
import org.koin.androidx.compose.koinViewModel

@Composable
fun UpdateDataInvoiceScreen(
    navController: NavController,
    viewModel: UpdateDataInvoiceViewModel = koinViewModel(),
) {
    // ===== Observe Download Result =====
    val updateDataResult by viewModel.updateDataResult.observeAsState(Result.success(null))

    var displayProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(updateDataResult.status, updateDataResult.progress, updateDataResult.data) {
        displayProgress = when (updateDataResult.status) {
            StatusNetwork.SUCCESS -> if (updateDataResult.data != null) 1f else 0f
            StatusNetwork.ERROR -> updateDataResult.progress.coerceIn(0f, 1f)
            else -> updateDataResult.progress.coerceIn(0f, 1f)
        }
    }

    // Efek Snackbar
    LaunchedEffect(updateDataResult.status, updateDataResult.data) {
        if (updateDataResult.status == StatusNetwork.SUCCESS && updateDataResult.data != null) {
            SnackbarManager.showSnackbar(SnackbarData("Update data selesai", SnackbarType.SUCCESS))
            viewModel.fetchConfigUpdated()
        } else if (updateDataResult.status == StatusNetwork.ERROR) {
            SnackbarManager.showSnackbar(
                SnackbarData(updateDataResult.message ?: "Terjadi kesalahan", SnackbarType.ERROR)
            )
            viewModel.fetchConfigUpdated()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.messageEvent.collect { message ->
            SnackbarManager.showSnackbar(SnackbarData(message, SnackbarType.SUCCESS))
        }
    }

    // Logika untuk Pesan dan Warna Card Result
    val (resultMessage, resultColor) = when {
        updateDataResult.status == StatusNetwork.LOADING -> {
            "Sedang memperbarui data..." to PieGray
        }

        updateDataResult.status == StatusNetwork.SUCCESS && updateDataResult.data != null -> {
            "Update data berhasil dengan data yang ditambahkan ke database dengan jumlah header : ${updateDataResult.data?.listCustomerInvoice?.size} dan jumlah nota : ${updateDataResult.data?.listNotaInvoice?.size}" to Primary
        }

        updateDataResult.status == StatusNetwork.ERROR -> {
            (updateDataResult.message ?: "Gagal melakukan update data") to LightRed
        }

        else -> {
            "Belum dilakukan update data" to Color.White
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
            title = "Update Data Tagihan", onBackClick = { navController.popBackStack() })

        Spacer(modifier = Modifier.height(Dimens.MediumMargin))

        // ===== Gauge =====
        GaugeChart(
            progress = displayProgress, strokeWidth = 80.dp, modifier = Modifier.fillMaxWidth()
        )

        // ===== Status Percentage Text =====
        Text(
            text = "${"%.0f".format(displayProgress * 100)}%", style = BodyPopBold.copy(
                fontSize = 25.sp, color = if (displayProgress >= 1f) Primary else Gray
            )
        )

        // ===== Card Result Update =====
        CardResultUpdateInvoice(
            backgroundColor = resultColor, message = resultMessage
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomPrimaryButton(
            text = stringResource(R.string.text_update_data),
            enabled = updateDataResult.status != StatusNetwork.LOADING,
            onClick = { viewModel.fetchUpdatedDataInvoice() })
    }
}

@Composable
fun CardResultUpdateInvoice(
    backgroundColor: Color,
    message: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(Dimens.MediumCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = message, style = BodyPopBold.copy(
                    color = if (backgroundColor == Color.White) Gray else Color.White,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}
