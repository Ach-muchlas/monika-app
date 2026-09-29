package com.sss.monikaapps.feature.result_download.presentation.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit,
) {
    var reason by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.Transparent
    ) {
        BackupDialogContent(
            reason = reason,
            onReasonChange = { reason = it },
            onClose = onDismiss,
            isLoading = loading,
            onSubmit = {
                loading = true
                onSubmit(reason)
            }
        )
    }
}
