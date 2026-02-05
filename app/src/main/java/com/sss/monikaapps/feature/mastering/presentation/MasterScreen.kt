package com.sss.monikaapps.feature.mastering.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_ACTIVITIES
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_EXPENSES
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_VISIT
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.home.data.model.HomeMenuItem
import com.sss.monikaapps.feature.home.ui.component.HomeMenuGrid

@Composable
fun MasterScreen(
    navController: NavController,
    onMenuClick: (Int) -> Unit,
) {

    val menuItems = listOf(
        HomeMenuItem(
            FEATURE_EXPENSES,
            stringResource(R.string.text_feature_expanses),
            stringResource(R.string.text_desc_master_feature_expanses),
            R.drawable.icon_expanses
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
            .padding(Dimens.MediumMargin)
    ) {

        CustomTopBar(
            title = "Master Data",
            onBackClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(Dimens.LargeMargin))
        HomeMenuGrid(menuItems = menuItems, onMenuClick = onMenuClick)
    }
}