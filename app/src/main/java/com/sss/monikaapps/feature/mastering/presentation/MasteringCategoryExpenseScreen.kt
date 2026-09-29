package com.sss.monikaapps.feature.mastering.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomNotFoundAnimation
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.mastering.presentation.component.CadItemMastering
import org.koin.androidx.compose.koinViewModel


@Composable
fun MasteringCategoryExpenseScreen(
    navController: NavController,
    viewModel: MasteringViewModel = koinViewModel(),
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.animation_loading)
    )

    val result by viewModel.masteringExpense.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchMasterExpense()
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
                title = stringResource(R.string.master_expense),
                onBackClick = { navController.popBackStack() },
            )

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            when (result?.status) {
                StatusNetwork.LOADING -> {
                    CustomLoadingDialog()
                }

                StatusNetwork.SUCCESS -> {
                    val masterData = result?.data?.data.orEmpty()

                    if (masterData.isEmpty()) {
                        Text("Tidak ada aktivitas", color = Color.Gray)
                    } else {
                        LazyColumn {
                            items(masterData) { data ->
                                CadItemMastering(data.name.toString())
                                Spacer(modifier = Modifier.height(Dimens.SmallMargin))
                            }
                        }
                    }
                }

                StatusNetwork.ERROR -> {
                    CustomNotFoundAnimation()
                }

                else -> Unit
            }
        }
    }

}