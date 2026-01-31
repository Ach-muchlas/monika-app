package com.sss.monikaapps.feature.visit.presentation.update

import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_VISIT
import com.sss.monikaapps.common.constanta.NameFeatureConstant.ACTIVITY
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
import com.sss.monikaapps.feature.visit.presentation.update.component.VisitForm
import org.koin.androidx.compose.koinViewModel

@Composable
fun UpdateVisitScreen(
    navController: NavController,
    typeVisit: String,
    idMobile: String? = null,
    viewModel: UpdateVisitViewModel = koinViewModel(),
    photoViewModel: PhotoViewModel = koinViewModel(),
    locationViewModel: LocationViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    Log.e("CHECK_DATA", "Data Id : $idMobile")
    val isCheckOut = typeVisit == CHECK_OUT

    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    val photoHelper = remember {
        PhotoHelper(context, ACTIVITY, typeVisit) { newPhoto ->
            photoViewModel.addPhoto(
                parentId = idMobile.toString(),
                parentType = typeVisit,
                parentFeature = FEATURE_VISIT,
                path = newPhoto
            )
        }
    }

    val launchCamera = photoHelper.rememberCameraLauncher()

    val photos by photoViewModel.observePhotos(idMobile.toString(), typeVisit).collectAsState()
    val locationState by locationViewModel.locationState.collectAsState()
    val checkInResult by viewModel.checkInResult.observeAsState()
    val checkOutResult by viewModel.checkOutResult.observeAsState()

    LaunchedEffect(Unit) {
        locationViewModel.fetchLocation()
    }

    LaunchedEffect(locationState?.status) {
        when (locationState?.status) {
            StatusNetwork.SUCCESS -> {
                val (lat, lng) = locationState!!.data!!
                latitude = lat.toString()
                longitude = lng.toString()
            }

            StatusNetwork.ERROR -> {
                SnackbarManager.showSnackbar(
                    SnackbarData(
                        when (locationState?.message) {
                            LocationError.Timeout.code -> "Gagal mengambil lokasi (Timeout)"
                            LocationError.NoLocation.code -> "Lokasi tidak ditemukan"
                            LocationError.NoPermission.code -> "Izin lokasi belum diberikan"
                            else -> "Gagal mengambil lokasi"
                        }, SnackbarType.ERROR
                    )
                )
            }

            else -> Unit
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
                title = if (isCheckOut) "Check Out Kunjungan" else "Check In Kunjungan",
                onBackClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                VisitForm(
                    typeVisit = typeVisit,
                    latitude = latitude,
                    longitude = longitude,
                    descVisit = desc,
                    onDescChange = { desc = it },
                    onLatChange = { latitude = it },
                    onLngChange = { longitude = it },
                    photos = photos.map { it.filePath },

                    onAddPhoto = { launchCamera() },

                    onDeletePhoto = { path ->
                        photos.firstOrNull { it.filePath == path }?.let {
                            photoViewModel.deletePhoto(it)
                        }
                    },

                    onRefreshLatLong = {
                        locationViewModel.fetchLocation()
                    },

                    onSubmit = {
                        if (isCheckOut) {
                            viewModel.checkOutVisit(
                                idVisit = idMobile.toString(),
                                endLat = latitude,
                                endLng = longitude
                            )
                        } else {
                            viewModel.checkInVisit(
                                idVisit = idMobile.toString(),
                                desc = desc,
                                startLat = latitude,
                                startLng = longitude
                            )
                        }
                    })
            }
        }

        if (locationState?.status == StatusNetwork.LOADING) {
            CustomLoadingDialog("Mengambil lokasi")
        }

        val resultAction = if (isCheckOut) checkOutResult else checkInResult
        resultAction?.let { result ->
            when (result.status) {
                StatusNetwork.LOADING -> {
                    CustomLoadingDialog("Loading mengirim ke server")
                }

                StatusNetwork.SUCCESS -> {
                    LaunchedEffect(result) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(result.data.toString(), SnackbarType.SUCCESS)
                        )
                        navController.popBackStack()
                    }
                }

                StatusNetwork.ERROR -> {
                    LaunchedEffect(result) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(result.message ?: "Terjadi kesalahan", SnackbarType.ERROR)
                        )
                    }
                }
            }
        }
    }
}
