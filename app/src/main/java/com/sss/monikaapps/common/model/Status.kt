package com.sss.monikaapps.common.model

import androidx.compose.ui.graphics.Color
import com.sss.monikaapps.R
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_OUT
import com.sss.monikaapps.common.theme.PieGray
import com.sss.monikaapps.common.theme.SelectedBackground
import com.sss.monikaapps.common.theme.Spruce

data class Status(
    val id: String,
    val title: String,
    val iconResource: Int,
    val bgColor: Color,
)

val dataStatusActivities = listOf(
    Status(CHECK_IN, "Check In", R.drawable.icon_check_in, Spruce),
    Status(CHECK_OUT, "Check Out", R.drawable.icon_check_out, Spruce),
)

val dataStatusExpanses = listOf(
    Status("7", "Semua", R.drawable.icon_profile, SelectedBackground),
    Status("0", "Dalam Proses", R.drawable.icon_process, PieGray),
    Status("4", "Diajukan", R.drawable.icon_submit, PieGray),
    Status("1", "Disetujui", R.drawable.icon_approve, PieGray),
    Status("2", "Terkonfirmasi", R.drawable.icon_confirm, PieGray),
    Status("5", "Ditolak", R.drawable.icon_reject, PieGray),
)

val dataStatusVisit = listOf(
    Status("7", "Semua", R.drawable.icon_profile, SelectedBackground),
    Status("0", "Belum dikunjungi", R.drawable.icon_profile, SelectedBackground),
    Status(CHECK_IN, "Check In", R.drawable.icon_notification, PieGray),
    Status(CHECK_OUT, "Check Out", R.drawable.icon_profile, SelectedBackground),
    Status("3", "Belum tersinkron", R.drawable.icon_profile, SelectedBackground),
)