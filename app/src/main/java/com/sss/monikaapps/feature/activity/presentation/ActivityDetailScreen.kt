package com.sss.monikaapps.feature.activity.presentation

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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.mapper.MapperActivity.resolveLatActivity
import com.sss.monikaapps.common.mapper.MapperActivity.resolveLngActivity
import com.sss.monikaapps.common.mapper.MapperActivity.resolveTimeActivity
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.model.dataStatusActivities
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.DarkRed
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.feature.activity.presentation.component.CardDetailItemActivity
import com.sss.monikaapps.feature.activity.presentation.component.CardHeaderActivityDetail
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActivityDetailScreen(
    navController: NavController,
    trno: String,
    idMobile: String,
    onCheckOutData: (trno: String, idMobile: String) -> Unit,
    clickDetailPhoto: (url: String) -> Unit,
    viewModel: ActivitiesViewModel = koinViewModel(),
) {
    val result by viewModel.detailActivityResult.observeAsState()
    var isStatus = false

    LaunchedEffect(Unit) {
        viewModel.fetchDetailActivity(idMobile)
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
                title = stringResource(R.string.text_detail_activity), onBackClick = {
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
                    val activity = result?.data
                    val header = activity?.header
                    val photos = activity?.fotoActivity.orEmpty()
                    isStatus = header?.isSync != "2"
                    val dataSyncLocal = header?.isSyncDataLocal

                    CardHeaderActivityDetail(
                        employeeName = header?.employeeId.toString(),
                        title = header?.title ?: "",
                        subtitle = header?.description ?: "",
                        checkInTime = header?.startAt ?: "",
                        checkOutTime = header?.endAt ?: ""
                    )

                    Spacer(modifier = Modifier.height(Dimens.LargeMargin))

                    dataStatusActivities.forEach { status ->
                        if (status.id == CHECK_OUT && isStatus) return@forEach

                        val photosByStatus = photos.filter { it.tipe == status.id }

                        val cardColor = when {
                            status.id == CHECK_IN && dataSyncLocal == 1 -> DarkRed
                            status.id == CHECK_OUT && dataSyncLocal == 3 -> DarkRed
                            header?.trnoMobile.isNullOrBlank() -> DarkRed
                            else -> Primary
                        }

                        CardDetailItemActivity(
                            title = status.title,
                            color = cardColor,
                            dateTime = status.resolveTimeActivity(header),
                            latitude = status.resolveLatActivity(header),
                            longitude = status.resolveLngActivity(header),
                            lisPhoto = photosByStatus,
                            clickDetailPhoto = clickDetailPhoto
                        )
                        Spacer(modifier = Modifier.height(Dimens.LargeMargin))
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

        if (isStatus) {
            CustomPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.MediumMargin),
                text = stringResource(R.string.text_checkout_activity),
            ) {
                onCheckOutData(trno, idMobile)
            }
        }
    }
}
