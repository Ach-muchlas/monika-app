package com.sss.monikaapps.feature.invoice.presentation.generate_pdf

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.invoice.presentation.generate_pdf.component.GeneratePdfForm
import org.koin.androidx.compose.koinViewModel

@Composable
fun GeneratePdfScreen(
    navController: NavController,
    viewModel: GeneratePdfViewModel = koinViewModel(),
) {

    var date by remember { mutableStateOf("") }

    val context = LocalContext.current

    val pdfResult by viewModel.pdfResult.collectAsStateWithLifecycle()

    LaunchedEffect(pdfResult) {
        pdfResult?.let { result ->
            when (result.status) {
                StatusNetwork.SUCCESS -> {
                    SnackbarManager.showSnackbar(
                        SnackbarData("PDF berhasil dibuat di: ${result.data}", SnackbarType.SUCCESS)
                    )
                    viewModel.clearPdfState()
                }

                StatusNetwork.ERROR -> {
                    SnackbarManager.showSnackbar(
                        SnackbarData(result.message ?: "Gagal membuat PDF", SnackbarType.ERROR)
                    )
                    viewModel.clearPdfState()
                }

                else -> Unit
            }
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
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            CustomTopBar(
                title = "Buat Pengeluaran",
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                GeneratePdfForm(
                    onDateChange = { date = it },
                ) {
                    if (pdfResult?.status != StatusNetwork.LOADING) {
                        viewModel.generatePdf(context, date)
                    }
                }
            }
        }

    }

    if (pdfResult?.status == StatusNetwork.LOADING) {
        CustomLoadingDialog(message = "Sedang membuat PDF...")
    }
}