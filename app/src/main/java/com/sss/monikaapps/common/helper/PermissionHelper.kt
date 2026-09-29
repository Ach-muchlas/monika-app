package com.sss.monikaapps.common.helper

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionHelper private constructor(
    private val activity: AppCompatActivity
) {
    private var requestMultiplePermissions: ActivityResultLauncher<Array<String>>? = null

    interface PermissionCallback {
        fun onPermissionGranted(permission: String)
        fun onPermissionDenied(permission: String)
    }

    private var permissionCallback: PermissionCallback? = null

    companion object {
        @Volatile
        private var instance: PermissionHelper? = null

        fun getInstance(activity: AppCompatActivity): PermissionHelper {
            return instance ?: synchronized(this) {
                instance ?: PermissionHelper(activity).also { instance = it }
            }
        }
    }

    fun setCallback(callback: PermissionCallback) {
        this.permissionCallback = callback
    }

    fun init() {
        if (requestMultiplePermissions == null) {
            requestMultiplePermissions =
                activity.registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    permissions.forEach { (permission, granted) ->
                        if (granted) {
                            permissionCallback?.onPermissionGranted(permission)
                        } else {
                            permissionCallback?.onPermissionDenied(permission)
                        }
                    }
                }
        }
    }

    fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)
        ) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (!isPermissionGranted(Manifest.permission.READ_PHONE_STATE)) {
            permissionsToRequest.add(Manifest.permission.READ_PHONE_STATE)
        }

        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (!isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestMultiplePermissions?.launch(permissionsToRequest.toTypedArray())
        }
    }

    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    fun release() {
        instance = null
        permissionCallback = null
        requestMultiplePermissions = null
    }
}
