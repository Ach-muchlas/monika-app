package com.sss.monikaapps.common.helper

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestAppPermissions(
    onPermissionDenied: (String) -> Unit = {},
) {
    val activity = LocalActivity.current ?: return
    val scope = rememberCoroutineScope()

    val permissions = buildList {
        add(Manifest.permission.CAMERA)

        add(Manifest.permission.ACCESS_FINE_LOCATION)
        add(Manifest.permission.ACCESS_COARSE_LOCATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
            add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        result.filterValues { !it }.keys.forEach { permission ->
            scope.launch {
                onPermissionDenied(permission)
            }
        }
    }

    DisposableEffect(Unit) {
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(
                activity, it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isNotEmpty()) {
            launcher.launch(notGranted.toTypedArray())
        }

        onDispose { }
    }
}
