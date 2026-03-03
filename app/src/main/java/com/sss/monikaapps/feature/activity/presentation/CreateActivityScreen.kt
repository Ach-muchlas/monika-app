package com.sss.monikaapps.feature.activity.presentation

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_ACTIVITIES
import com.sss.monikaapps.common.constanta.NameFeatureConstant.ACTIVITY
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.helper.GenerateRandomTextHelper.generateRandomId
import com.sss.monikaapps.common.helper.PhotoHelper
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.result.LocationError
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.viewmodel.LocationViewModel
import com.sss.monikaapps.common.viewmodel.PhotoViewModel
import com.sss.monikaapps.feature.activity.data.handler.ActivitySubmitHandler
import com.sss.monikaapps.feature.activity.presentation.component.ActivityForm
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateActivityScreen(
    navController: NavController,
    typeActivity: String,
    trno: String? = null,
    idMobile: String? = null,
    photoViewModel: PhotoViewModel = koinViewModel(),
    viewModel: ActivitiesViewModel = koinViewModel(),
    locationViewModel: LocationViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    val isCheckOut = trno != null && typeActivity == CHECK_OUT

    val generatedId = rememberSaveable { generateRandomId() }
    val parentId = if (isCheckOut) trno!! else generatedId
    val parentIdPhoto = if (isCheckOut) idMobile!! else generatedId

    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    val photoHelper = remember {
        PhotoHelper(context, ACTIVITY, typeActivity) { newPhoto ->
            photoViewModel.addPhoto(
                parentId = parentIdPhoto,
                parentType = typeActivity,
                parentFeature = FEATURE_ACTIVITIES,
                path = newPhoto
            )
        }
    }

    val launchCamera = photoHelper.rememberCameraLauncher()

    val photos by photoViewModel
        .observePhotos(parentIdPhoto, typeActivity)
        .collectAsState()

    val createResult by viewModel.createResult.observeAsState()
    val updateResult by viewModel.updateResult.observeAsState()

    val activeResult = if (isCheckOut) updateResult else createResult

    val locationState by locationViewModel.locationState.collectAsState()

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
                title = if (isCheckOut) "Selesai Aktivitas" else "Mulai Aktivitas",
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                ActivityForm(
                    typeActivity = typeActivity,
                    titleActivity = title,
                    descActivity = desc,
                    onTitleChange = { title = it },
                    onDescChange = { desc = it },
                    photos = photos.map { it.filePath },

                    onAddPhoto = { launchCamera() },

                    onDeletePhoto = { path ->
                        photos.firstOrNull { it.filePath == path }?.let {
                            photoViewModel.deletePhoto(it)
                        }
                    },

                    onSubmit = {
                        locationViewModel.fetchLocation()
                    }
                )
            }
        }

        LaunchedEffect(locationState) {
            when (locationState?.status) {
                StatusNetwork.SUCCESS -> {
                    val (lat, lng) = locationState!!.data!!

                    ActivitySubmitHandler.submit(
                        isCheckOut = isCheckOut,
                        idMobile = idMobile.toString(),
                        activityId = parentId,
                        title = title,
                        desc = desc,
                        lat = lat.toString(),
                        lng = lng.toString(),
                        viewModel = viewModel,
                    )

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

        activeResult?.let { result ->
            when (result.status) {
                StatusNetwork.LOADING -> {
                    CustomLoadingDialog("Loading mengirim ke server")
                }

                StatusNetwork.SUCCESS -> {
                    LaunchedEffect(result) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(result.data.toString(), SnackbarType.SUCCESS)
                        )
                        viewModel.clearCreateState()
                        viewModel.clearUpdateState()
                        navController.popBackStack()
                    }
                }

                StatusNetwork.ERROR -> {
                    LaunchedEffect(result) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(result.message ?: "Terjadi kesalahan", SnackbarType.ERROR)
                        )
                        viewModel.clearCreateState()
                        viewModel.clearUpdateState()
                    }
                }
            }
        }
    }
}

