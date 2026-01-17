package com.sss.monikaapps.common.model

import com.sss.monikaapps.common.data.SnackbarType


data class SnackbarData(
    val message: String,
    val type: SnackbarType = SnackbarType.INFO,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
    val duration: Long = 3000L
)