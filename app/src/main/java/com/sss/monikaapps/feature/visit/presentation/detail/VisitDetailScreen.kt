package com.sss.monikaapps.feature.visit.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.helper.MapsHelper.openGoogleMaps
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.model.dataStatusActivities
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.DarkRed
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.feature.activity.presentation.component.CardDetailItemActivity
import com.sss.monikaapps.feature.download.data.mapper.VisitMapper
import com.sss.monikaapps.feature.download.data.mapper.VisitMapper.resolveLatVisit
import com.sss.monikaapps.feature.download.data.mapper.VisitMapper.resolveLngVisit
import com.sss.monikaapps.feature.download.data.mapper.VisitMapper.resolveTimeVisit
import com.sss.monikaapps.feature.visit.presentation.detail.component.CardHeaderVisitDetail
import org.koin.androidx.compose.koinViewModel

@Composable
fun VisitDetailScreen(
    idVisit: String,
    navController: NavController,
    viewModel: VisitDetailViewModel = koinViewModel(),
    onClickButton: (idVisit: String, typeForm: String) -> Unit,
    clickDetailPhoto: (url: String) -> Unit,
) {
    val context = LocalContext.current

    val result by viewModel.visitDetailResult.observeAsState()
    var isStatus = 0

    var customerLat = "0"
    var customerLng = "0"

    LaunchedEffect(Unit) {
        viewModel.fetchVisitDetail(idVisit)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(Dimens.MediumMargin)
        ) {

            CustomTopBar(
                title = stringResource(R.string.text_detail_visit), onBackClick = {
                    navController.popBackStack()
                })

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            when (result?.status) {
                StatusNetwork.LOADING -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                StatusNetwork.SUCCESS -> {
                    val visit = result?.data?.header
                    val photos = result?.data?.photos
                    isStatus = visit?.syncStatus ?: 0
                    customerLat = visit?.customerLatitude ?: "0"
                    customerLng = visit?.customerLongitude ?: "0"

                    val dataStatusSync = when (isStatus) {
                        1, 2 -> true
                        3, 4 -> false
                        else -> false
                    }

                    CardHeaderVisitDetail(
                        customerId = visit?.customerId.toString(),
                        customerName = visit?.customerName.toString(),
                        address = visit?.address ?: "-",
                        latitude = visit?.customerLatitude ?: "0",
                        longitude = visit?.customerLongitude ?: "0",
                        checkInTime = visit?.startAt ?: "",
                        checkOutTime = visit?.endAt ?: "",
                    )
                    Spacer(modifier = Modifier.height(Dimens.LargeMargin))

                    if (isStatus != 0) {
                        dataStatusActivities.forEach { status ->

                            if (status.id == CHECK_OUT && dataStatusSync) return@forEach

                            val photosByStatus = photos?.filter { it.parentType == status.id }
                            val cardColor = when {
                                visit?.trno?.isBlank() == true -> DarkRed
                                status.id == CHECK_IN && isStatus == 1 -> DarkRed
                                status.id == CHECK_OUT && isStatus == 3 -> DarkRed
                                else -> Primary
                            }

                            CardDetailItemActivity(
                                title = status.title,
                                color = cardColor,
                                dateTime = status.resolveTimeVisit(visit),
                                latitude = status.resolveLatVisit(visit),
                                longitude = status.resolveLngVisit(visit),
                                lisPhoto = VisitMapper.photoEntityToPhotoItem(photosByStatus),
                                clickDetailPhoto = clickDetailPhoto
                            )

                            Spacer(modifier = Modifier.height(Dimens.LargeMargin))

                        }
                    }
                }

                StatusNetwork.ERROR -> {
                    LaunchedEffect(result?.message) {
                        SnackbarManager.showSnackbar(
                            SnackbarData(
                                result?.message ?: "Gagal mengambil aktivitas", SnackbarType.ERROR
                            )
                        )
                    }
                }

                else -> Unit
            }
        }


        if (isStatus <= 2) {
            CustomPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.MediumMargin),
                text = if (isStatus == 0) stringResource(R.string.text_checkin_visit) else stringResource(
                    R.string.text_checkout_visit
                ),
            ) {
                val type = when (isStatus) {
                    0 -> "1"
                    1, 2 -> "2"
                    else -> "0"
                }
                onClickButton(idVisit, type)
            }
        }

        CustomPrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.MediumMargin),
            text = stringResource(R.string.text_gmaps),
            colors = listOf(
                Color(0xFF5ED293),
                Color(0xFF27AE60),
                Color(0xFF1E8449)
            ),
            onClick = {
                openGoogleMaps(context, lat = customerLat, lng = customerLng)
            }
        )
    }
}

