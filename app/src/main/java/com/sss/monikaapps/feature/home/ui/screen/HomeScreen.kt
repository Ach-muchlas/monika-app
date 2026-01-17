package com.sss.monikaapps.feature.home.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sss.monikaapps.R
import com.sss.monikaapps.common.constanta.HomeFeatureConstant
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_ACTIVITIES
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_EXPANSES
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_MASTER_DATA
import com.sss.monikaapps.common.constanta.HomeFeatureConstant.FEATURE_VISIT
import com.sss.monikaapps.feature.home.presentasi.HomeViewModel
import com.sss.monikaapps.feature.home.ui.component.HomeHeader
import com.sss.monikaapps.feature.home.ui.component.HomeMenuGrid
import com.sss.monikaapps.feature.home.data.model.HomeMenuItem
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onMenuClick: (Int) -> Unit,
) {
    val user by viewModel.user.collectAsState()

    val menuItems = listOf(
        HomeMenuItem(
            FEATURE_ACTIVITIES,
            stringResource(R.string.text_feature_activity),
            stringResource(R.string.text_desc_feature_activity), R.drawable.icon_activity
        ),
        HomeMenuItem(
            FEATURE_VISIT,
            stringResource(R.string.text_feature_visited),
            stringResource(R.string.text_desc_feature_visited), R.drawable.icon_visited
        ),
        HomeMenuItem(
            FEATURE_EXPANSES,
            stringResource(R.string.text_feature_expanses),
            stringResource(R.string.text_desc_feature_expanses),
            R.drawable.icon_expanses
        ),
        HomeMenuItem(
            FEATURE_MASTER_DATA,
            stringResource(R.string.text_feature_master_data),
            stringResource(R.string.text_desc_feature_master_data), R.drawable.icon_data_mastering
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
            .padding(Dimens.MediumMargin)
    ) {
        HomeHeader(
            userName = user.employeeName ?: "-",
            userRole = user.roleName ?: "-",
            nameDepo = user.namaDepo ?: "-"
        )
        Spacer(modifier = Modifier.height(Dimens.LargeMargin))
        HomeMenuGrid(menuItems = menuItems, onMenuClick = onMenuClick)
    }
}