package com.sss.monikaapps.feature.invoice.presentation.payment

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomLoadingView
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_INVOICE
import com.sss.monikaapps.common.constanta.NameFeatureConstant.INVOICE
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.formatter.FormatterCurrency.formatCurrency
import com.sss.monikaapps.common.helper.PhotoHelper
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.result.LocationError
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.viewmodel.LocationViewModel
import com.sss.monikaapps.common.viewmodel.PhotoViewModel
import com.sss.monikaapps.feature.invoice.presentation.payment.component.PaymentInvoiceForm
import org.koin.androidx.compose.koinViewModel

@Composable
fun PaymentInvoiceScreen(
    navController: NavController,
    viewModel: PaymentInvoiceViewModel = koinViewModel(),
    photoViewModel: PhotoViewModel = koinViewModel(),
    locationViewModel: LocationViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    val locationState by locationViewModel.locationState.collectAsState()
    val payment by viewModel.paymentData.collectAsStateWithLifecycle()
    val reasonState by viewModel.reason.collectAsStateWithLifecycle()
    val submitState by viewModel.submitState.collectAsStateWithLifecycle()

    val statusPaid by viewModel.statusPaid.collectAsStateWithLifecycle()
    val totalPaid by viewModel.totalPaid.collectAsStateWithLifecycle()
    val selectedReason by viewModel.selectedReason.collectAsStateWithLifecycle()


    val photoHelper = remember {
        PhotoHelper(context, INVOICE, viewModel.nomorNota) { newPhoto ->
            photoViewModel.addPhoto(
                parentId = viewModel.idInvoice,
                parentType = viewModel.nomorNota,
                parentFeature = FEATURE_INVOICE,
                path = newPhoto
            )
        }
    }

    val launchCamera = photoHelper.rememberCameraLauncher()

    val photos by photoViewModel
        .observePhotos(viewModel.idInvoice, viewModel.nomorNota)
        .collectAsState()



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
                stringResource(R.string.text_payment_invoice),
                onBackClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                when (payment.status) {
                    StatusNetwork.LOADING -> {
                        CustomLoadingView()
                    }

                    StatusNetwork.SUCCESS -> {
                        val dataPayment = payment.data

                        val listReason = if (reasonState.status == StatusNetwork.SUCCESS) {
                            reasonState.data ?: emptyList()
                        } else {
                            emptyList()
                        }

                        PaymentInvoiceForm(
                            customerData = "${dataPayment?.customerId} - ${dataPayment?.customerName}",
                            nomorNota = viewModel.nomorNota,
                            totalOutstanding = formatCurrency(
                                (dataPayment?.outstandingNota ?: 0).toLong()
                            ),

                            // HUBUNGKAN DENGAN VIEWMODEL
                            statusPaid = statusPaid,
                            totalPaid = totalPaid,
                            reasons = listReason,
                            selectedReason = selectedReason,

                            onPaidChange = viewModel::onPaidChange,
                            onStatusPaidChange = viewModel::onStatusPaidChange,
                            onReasonChange = viewModel::onReasonChange,

                            photos = photos.map { it.filePath },
                            onAddPhoto = { launchCamera() },
                            onDeletePhoto = { path ->
                                photos.firstOrNull { it.filePath == path }?.let {
                                    photoViewModel.deletePhoto(it)
                                }
                            },
                            onSubmit = { locationViewModel.fetchLocation() }
                        )
                    }

                    StatusNetwork.ERROR -> {

                    }
                }
            }
        }

        LaunchedEffect(locationState) {
            when (locationState?.status) {
                StatusNetwork.SUCCESS -> {
                    val (lat, lng) = locationState!!.data!!

                    viewModel.submit(lat.toString(), lng.toString())

                    locationViewModel.clearState()
                }

                StatusNetwork.ERROR -> {
                    SnackbarManager.showSnackbar(
                        SnackbarData(
                            when (locationState?.message) {
                                LocationError.Timeout.code -> "Gagal mengambil lokasi (Timeout)"
                                LocationError.NoLocation.code -> "Lokasi tidak ditemukan"
                                LocationError.NoPermission.code -> "Izin lokasi belum diberikan"
                                else -> "Gagal mengambil lokasi"
                            },
                            SnackbarType.ERROR
                        )
                    )

                    locationViewModel.clearState()
                }

                else -> {}
            }
        }

        if (locationState?.status == StatusNetwork.LOADING) {
            CustomLoadingDialog("Mengambil lokasi")
        }

        when (submitState?.status) {
            StatusNetwork.LOADING -> {
                CustomLoadingDialog("Loading mengirim ke server")
            }

            StatusNetwork.SUCCESS -> {
                LaunchedEffect(submitState) {
                    SnackbarManager.showSnackbar(
                        SnackbarData(
                            submitState?.data ?: "Berhasil simpan data ",
                            SnackbarType.SUCCESS
                        )
                    )
                    navController.popBackStack()
                }

            }

            StatusNetwork.ERROR -> {
                LaunchedEffect(submitState) {
                    SnackbarManager.showSnackbar(
                        SnackbarData(
                            submitState?.message ?: "Terjadi kesalahan",
                            SnackbarType.ERROR
                        )
                    )
                    navController.popBackStack()
                }
            }

            else -> {}
        }
    }
}