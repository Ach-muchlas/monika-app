package com.sss.monikaapps.feature.invoice.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomLoadingView
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.formatter.FormatterCurrency.formatCurrency
import com.sss.monikaapps.common.formatter.FormatterDate.formatDateToIndoDisplay
import com.sss.monikaapps.common.helper.MapsHelper.openGoogleMaps
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.model.dataStatusInvoice
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.invoice.presentation.detail.component.CardDetailItemNotaInvoice
import com.sss.monikaapps.feature.invoice.presentation.detail.component.CardHeaderInvoiceDetail
import org.koin.androidx.compose.koinViewModel

@Composable
fun InvoiceDetailScreen(
    navController: NavController,
    viewModel: DetailInvoiceViewModel = koinViewModel(),
    onClickPayment: (idInvoice: String, nomorNota: String, customerId: String) -> Unit,
    clickDetailPhoto: (url: String) -> Unit,
) {
    val resultDetail by viewModel.invoiceDetail.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Gunakan state untuk koordinat agar reaktif saat tombol diklik
    val customerLat = resultDetail.data?.customerInvoice?.gpsLatCustomer ?: "0"
    val customerLng = resultDetail.data?.customerInvoice?.gpsLngCustomer ?: "0"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
    ) {
        when (resultDetail.status) {
            StatusNetwork.LOADING -> {
                CustomLoadingView()
            }

            StatusNetwork.SUCCESS -> {
                val detail = resultDetail.data ?: return

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Dimens.MediumMargin)
                ) {
                    // 1. TopBar
                    item {
                        CustomTopBar(
                            title = "Detail Invoice",
                            onBackClick = { navController.popBackStack() }
                        )
                        Spacer(modifier = Modifier.height(Dimens.MediumMargin))
                    }

                    item {
                        CardHeaderInvoiceDetail(data = detail.customerInvoice)
                        Spacer(modifier = Modifier.height(Dimens.LargeMargin))
                    }
                    items(detail.listNota) { item ->
                        val statusDisplay =
                            dataStatusInvoice.find { it.id == item.nota.status.toString() }?.title
                                ?: "Belum Selesai"

                        CardDetailItemNotaInvoice(
                            idInvoice = item.nota.id,
                            nomorNota = item.nota.nomorNota,
                            outstanding = formatCurrency(item.nota.outstandingNota.toLong()),
                            statusId = item.nota.status,
                            syncStatus = item.nota.syncStatus,
                            status = statusDisplay,
                            dueDate = formatDateToIndoDisplay(item.nota.dueDate),
                            dateNote = formatDateToIndoDisplay(item.nota.dateNota),
                            textReasonOrTotal = if (item.nota.status == 1) formatCurrency(item.nota.moneyPaid) else item.nota.reason.toString(),
                            lisPhoto = item.lisPhoto,
                            clickPayment = { idInvoice, nota ->
                                if (item.nota.status == 0) {
                                    onClickPayment.invoke(
                                        idInvoice,
                                        nota,
                                        viewModel.customerId
                                    )
                                }
                            },
                            clickDetailPhoto = clickDetailPhoto
                        )
                        Spacer(modifier = Modifier.height(Dimens.MediumMargin))
                    }
                }
            }

            StatusNetwork.ERROR -> {
                LaunchedEffect(resultDetail.message) {
                    SnackbarManager.showSnackbar(
                        SnackbarData(
                            resultDetail.message ?: "Gagal mengambil detail",
                            SnackbarType.ERROR
                        )
                    )
                }
            }
        }

        // 4. Tombol statis di bawah (Sticky Bottom)
        CustomPrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumMargin),
            text = stringResource(R.string.text_gmaps),
            colors = listOf(Color(0xFFFFC107), Color(0xFF8BC34A), Color(0xFFB1AA00)),
            onClick = {
                openGoogleMaps(context, lat = customerLat, lng = customerLng)
            }
        )
    }
}