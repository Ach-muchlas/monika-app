package com.sss.monikaapps.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.snackbar.SnackbarManager
import kotlinx.coroutines.delay


@Composable
fun SnackbarHostView() {

    var snackbarData by remember { mutableStateOf<SnackbarData?>(null) }

    LaunchedEffect(Unit) {
        SnackbarManager.snackbarFlow.collect { data ->
            snackbarData = data
            delay(data.duration)
            snackbarData = null
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        snackbarData?.let {
            CustomSnackbar(
                data = it,
                onDismiss = { snackbarData = null }
            )
        }
    }
}
