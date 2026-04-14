package com.sss.monikaapps.feature.invoice.presentation.payment

import android.annotation.SuppressLint
import android.os.Looper
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomTopBar
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
import com.sss.monikaapps.feature.invoice.presentation.payment.component.PaymentInvoiceForm
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import org.koin.androidx.compose.koinViewModel
import kotlin.coroutines.resume


@Composable
fun PaymentInvoiceScreen(
    navController: NavController,
    viewModel: PaymentInvoiceViewModel = koinViewModel(),
    photoViewModel: PhotoViewModel = koinViewModel(),
    locationViewModel: LocationViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    val locationState by locationViewModel.locationState.collectAsState()
//    val payment by viewModel.paymentData.collectAsStateWithLifecycle()
//    val reasonState by viewModel.reason.collectAsStateWithLifecycle()
    val submitState by viewModel.submitState.collectAsStateWithLifecycle()

    var date by remember { mutableStateOf("-") }
//    val statusPaid by viewModel.statusPaid.collectAsStateWithLifecycle()

//    val totalPaid by viewModel.totalPaid.collectAsStateWithLifecycle()
//    val selectedReason by viewModel.selectedReason.collectAsStateWithLifecycle()
//    val selectedBankReceipt by viewModel.selectedBankReceipt.collectAsStateWithLifecycle()
//    val paymentMethod by viewModel.paymentMethod.collectAsStateWithLifecycle()
//    val bankReceipts by viewModel.bankReceipts.collectAsState()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val parentType = when (state.statusPaid) {
        1 -> PAID_TRANSFER
        2 -> NOT_PAID
        3 -> RECEIPT
        else -> 0
    }


    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val photoHelper = remember(fusedLocationClient) {
        PhotoHelper(
            context = context,
            featureName = INVOICE,
            typeFeature = viewModel.nomorNota,
            enableWatermark = true,

            getLocation = {
                getCurrentLocation(fusedLocationClient)
            },

            onPhotoTaken = { newPhoto ->
                photoViewModel.addPhoto(
                    parentId = viewModel.idInvoice,
                    parentType = viewModel.nomorNota,
                    parentFeature = NOT_PAID,
                    path = newPhoto
                )
            }
        )
    }

    val photoHelperForReceipt = remember {
        PhotoHelper(context, INVOICE, viewModel.nomorNota) { newPhoto ->
            photoViewModel.addPhoto(
                parentId = viewModel.idInvoice,
                parentType = viewModel.nomorNota,
                parentFeature = RECEIPT,
                path = newPhoto
            )
        }
    }

    val photoHelperEvidence = remember {
        PhotoHelper(context, INVOICE, viewModel.nomorNota) { newPhoto ->
            photoViewModel.addPhoto(
                parentId = viewModel.idInvoice,
                parentType = viewModel.nomorNota,
                parentFeature = PAID_TRANSFER,
                path = newPhoto
            )
        }
    }

    val launchCamera = photoHelper.rememberCameraLauncher()
    val launchCameraForReceipt = photoHelperForReceipt.rememberCameraLauncher()
    val launchCameraEvidencePaidTransfer = photoHelperEvidence.rememberCameraLauncher()

    val photos by photoViewModel
        .observerPhotosInvoice(viewModel.idInvoice, viewModel.nomorNota, parentType)
        .collectAsState()

    val uiState = state.copy(
        photos = photos.map { it.filePath }
    )

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
                                launchCamera()
                            }

                            is PaymentInvoiceEvent.OnAddPhotoReceipt -> {
                                launchCameraForReceipt()
                            }

                            is PaymentInvoiceEvent.OnAddPhotoTransfer -> {
                                launchCameraEvidencePaidTransfer()
                            }

                            is PaymentInvoiceEvent.OnDeletePhoto -> {
                                photos.firstOrNull { it.filePath == event.path }?.let {
                                    photoViewModel.deletePhoto(it)
                                }
                            }

                            is PaymentInvoiceEvent.OnSubmit -> {
                                locationViewModel.fetchLocation()
                            }

                            is PaymentInvoiceEvent.OnDateChange -> {
                                date = event.date
                            }

                            else -> viewModel.onEvent(event)
                        }
                    }
                )

//                when (payment.status) {
//                    StatusNetwork.LOADING -> {
//                        CustomLoadingView()
//                    }
//
//                    StatusNetwork.SUCCESS -> {
//                        val dataPayment = payment.data
//
//                        val listReason = if (reasonState.status == StatusNetwork.SUCCESS) {
//                            reasonState.data ?: emptyList()
//                        } else {
//                            emptyList()
//                        }
//                        val uiState = state.copy(
//                            customerData = "${dataPayment?.customerId} - ${dataPayment?.customerName}",
//                            nomorNota = viewModel.nomorNota,
//                            totalOutstanding = formatCurrency((dataPayment?.outstandingNota ?: 0).toLong()),
//                            totalNominalNota = formatCurrency(dataPayment?.nominalNota?.toLong() ?: 0),
//                            photos = photos.map { it.filePath },
//                            reasons = listReason ?: emptyList(),
//                            listBankReceipt = bankReceipts ?: emptyList()
//                        )
//
//                        PaymentInvoiceForm(
//                            customerData = "${dataPayment?.customerId} - ${dataPayment?.customerName}",
//                            nomorNota = viewModel.nomorNota,
//                            totalOutstanding = formatCurrency(
//                                (dataPayment?.outstandingNota ?: 0).toLong()
//                            ),
//                            totalNominalNota = formatCurrency(
//                                dataPayment?.nominalNota?.toLong() ?: 0
//                            ),
//
//                            onDateChange = { date = it },
//
//                            statusPaid = statusPaid,
//                            totalPaid = totalPaid,
//                            reasons = listReason,
//                            selectedReason = selectedReason,
//                            selectedBankReceipt = selectedBankReceipt,
//                            paymentMethod = paymentMethod,
//
//                            listBankReceipt = bankReceipts,
//
//                            onPaidChange = viewModel::onPaidChange,
//                            onStatusPaidChange = viewModel::onStatusPaidChange,
//                            onReasonChange = viewModel::onReasonChange,
//                            onBankReceiptSelected = viewModel::onBankChange,
//                            onPaymentMethodChange = viewModel::onPaymentMethodChange,
//
//                            photos = photos.map { it.filePath },
//                            onAddPhoto = { launchCamera() },
//                            onAddPhotoForReceipt = { launchCameraForReceipt() },
//                            onAddPhotoEvidenceTransfer = { launchCameraEvidencePaidTransfer() },
//                            onDeletePhoto = { path ->
//                                photos.firstOrNull { it.filePath == path }?.let {
//                                    photoViewModel.deletePhoto(it)
//                                }
//                            },
//
//                            onSubmit = { locationViewModel.fetchLocation() }
//                        )
//                    }
//
//                    StatusNetwork.ERROR -> {
//
//                    }
//                }
            }
        }

        LaunchedEffect(locationState) {
            when (locationState?.status) {
                StatusNetwork.SUCCESS -> {
                    val (lat, lng) = locationState!!.data!!

                    viewModel.submit(lat.toString(), lng.toString(), date)

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

@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(
    client: FusedLocationProviderClient,
): Pair<Double, Double>? {

    val lastLocation = client.lastLocation.await()

    if (lastLocation != null) {
        return Pair(lastLocation.latitude, lastLocation.longitude)
    }

    return suspendCancellableCoroutine { cont ->

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000
        ).setMaxUpdates(1).build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                client.removeLocationUpdates(this)
                val loc = result.lastLocation
                cont.resume(loc?.let { Pair(it.latitude, it.longitude) })
            }
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
    }
}