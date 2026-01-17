package com.sss.monikaapps.common.model

import androidx.compose.ui.graphics.Color
import com.sss.monikaapps.R
import com.sss.monikaapps.common.theme.PieGray
import com.sss.monikaapps.common.theme.SelectedBackground
import com.sss.monikaapps.utils.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.utils.constanta.FeatureActivityConstant.CHECK_OUT


data class Status(
    val id: String,
    val title: String,
    val iconResource: Int,
    val bgColor: Color,
)

val dataStatusActivities = listOf(
    Status(CHECK_IN, "Check In", R.drawable.icon_notification, PieGray),
    Status(CHECK_OUT, "Check Out", R.drawable.icon_profile, SelectedBackground),
)

val dataStatusExpanses = listOf(
    Status("7", "All", R.drawable.icon_profile, SelectedBackground),
    Status("0", "In Process", R.drawable.icon_notification, PieGray),
    Status("1", "Approved", R.drawable.icon_notification, PieGray),
    Status("2", "Confirmed", R.drawable.icon_notification, PieGray),
    Status("5", "Void", R.drawable.icon_notification, PieGray),
)
