package com.sss.monikaapps.feature.invoice.presentation.payment

import android.content.Context
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
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.constanta.InvoiceStatusPayment.BG_CHECK
import com.sss.monikaapps.common.constanta.InvoiceStatusPayment.NOT_PAID
import com.sss.monikaapps.common.constanta.InvoiceStatusPayment.PAID_TRANSFER
import com.sss.monikaapps.common.constanta.InvoiceStatusPayment.RECEIPT
import com.sss.monikaapps.common.constanta.NameFeatureConstant.INVOICE
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.helper.PhotoHelper
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.result.LocationError
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.viewmodel.LocationViewModel
import com.sss.monikaapps.common.viewmodel.PhotoViewModel
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst
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
    val submitState by viewModel.submitState.collectAsStateWithLifecycle()

    val state by viewModel.state.collectAsStateWithLifecycle()

    val parentType = when (state.statusPaid) {
        1 -> PAID_TRANSFER
        2 -> NOT_PAID
        3 -> RECEIPT
        4 -> BG_CHECK
        else -> 0
    }

    LaunchedEffect(Unit) {
        viewModel.requestLocation.collect {
            locationViewModel.fetchLocation()
        }
    }

    val photoHelper = rememberPhotoHelper(
        context, viewModel, photoViewModel, NOT_PAID
    )

    val photoHelperForReceipt = rememberPhotoHelper(
        context, viewModel, photoViewModel, RECEIPT
    )

    val photoHelperEvidence = rememberPhotoHelper(
        context, viewModel, photoViewModel, PAID_TRANSFER
    )

    val photoHelperForBG = rememberPhotoHelper(
        context, viewModel, photoViewModel, BG_CHECK
    )

    val launchCamera = photoHelper.rememberCameraLauncher()
    val launchCameraForReceipt = photoHelperForReceipt.rememberCameraLauncher()
    val launchCameraEvidencePaidTransfer = photoHelperEvidence.rememberCameraLauncher()
    val launchCameraBG = photoHelperForBG.rememberCameraLauncher()

    val photosFlow = remember(viewModel.idInvoice, viewModel.nomorNota, parentType) {
        photoViewModel.observerPhotosInvoice(viewModel.idInvoice, viewModel.nomorNota, parentType)
    }

    val photos by photosFlow.collectAsStateWithLifecycle()

    val uiState = state.copy(photos = photos.map { it.filePath })

    LaunchedEffect(photos.size) {
        if (photos.isNotEmpty()) {
            viewModel.onEvent(PaymentInvoiceEvent.ClearPhotoError)
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
                stringResource(R.string.text_payment_invoice),
                onBackClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                PaymentInvoiceForm(
                    state = uiState,
                    onEvent = { event ->

                        when (event) {

                            is PaymentInvoiceEvent.OnAddPhoto -> {
                                when (uiState.statusPaid) {
                                    PaymentStatusConst.STATUS_UNPAID -> launchCamera()
                                    PaymentStatusConst.STATUS_RECEIPT -> launchCameraForReceipt()
                                    PaymentStatusConst.STATUS_PAID -> launchCameraEvidencePaidTransfer()
                                    PaymentStatusConst.STATUS_BG_CHECK -> launchCameraBG()
                                }
                            }

                            is PaymentInvoiceEvent.OnDeletePhoto -> {
                                photos.firstOrNull { it.filePath == event.path }?.let {
                                    photoViewModel.deletePhoto(it)
                                }
                            }

                            else -> viewModel.onEvent(event)
                        }
                    }
                )
            }
        }

        LaunchedEffect(locationState) {
            when (locationState?.status) {
                StatusNetwork.SUCCESS -> {
                    val (lat, lng) = locationState!!.data!!

                    viewModel.onEvent(
                        PaymentInvoiceEvent.OnLocationResult(
                            lat.toString(),
                            lng.toString()
                        )
                    )
                    locationViewModel.clearState()
                }

                StatusNetwork.ERROR -> {
                    SnackbarManager.showSnackbar(
                        SnackbarData(
                            when (locationState?.message) {
                                LocationError.Timeout.code -> context.getString(R.string.text_timeout_get_location)
                                LocationError.NoLocation.code -> context.getString(R.string.text_location_not_found)
                                LocationError.NoPermission.code -> context.getString(R.string.text_permission_not_found)
                                else -> context.getString(R.string.text_error_get_location)
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


@Composable
fun rememberPhotoHelper(
    context: Context,
    viewModel: PaymentInvoiceViewModel,
    photoViewModel: PhotoViewModel,
    parentFeature: Int,
): PhotoHelper {
    return remember(parentFeature) {
        PhotoHelper(
            context = context,
            featureName = INVOICE,
            typeFeature = viewModel.nomorNota,
            onPhotoTaken = { newPhoto ->
                photoViewModel.addPhoto(
                    parentId = viewModel.idInvoice,
                    parentType = viewModel.nomorNota,
                    parentFeature = parentFeature,
                    path = newPhoto
                )
            }
        )
    }
}