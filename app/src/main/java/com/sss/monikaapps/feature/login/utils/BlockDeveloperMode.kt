package com.sss.monikaapps.feature.login.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun BlockIfDeveloperModeEnabled(
    onBlocked: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (DeveloperModeUtils.isDeveloperModeEnabled(context)) {
            onBlocked()
        }
    }
}
