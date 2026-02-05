package com.sss.monikaapps.feature.login.ui.screen

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.helper.RequestAppPermissions
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.CardWhite
import com.sss.monikaapps.feature.login.presentation.AuthViewModel
import com.sss.monikaapps.feature.login.ui.component.LoginFooter
import com.sss.monikaapps.feature.login.ui.component.LoginForm
import com.sss.monikaapps.feature.login.ui.component.LoginHeader
import com.sss.monikaapps.feature.login.utils.CheckDeveloperModeBottomSheet
import com.sss.monikaapps.feature.login.utils.DeveloperModeUtils
import com.sss.monikaapps.feature.login.utils.HandleLoginResult
import com.sss.monikaapps.feature.utils.device.presentation.DeviceViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = koinViewModel(),
    deviceViewModel: DeviceViewModel = koinViewModel(),
    onSuccessLoginSuccess: () -> Unit,
    onSettingConnection: () -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    val imei by deviceViewModel.deviceId.observeAsState("")
    val versionApps by deviceViewModel.versionApps.observeAsState("")
    var employeeId by remember { mutableStateOf("") }
    val loginState by viewModel.loginResult.observeAsState()

    var showDevModeSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (DeveloperModeUtils.isDeveloperModeEnabled(context)) {
            showDevModeSheet = true
        }
    }

    if (showDevModeSheet) {
        CheckDeveloperModeBottomSheet()
    }

    RequestAppPermissions(
        onPermissionDenied = { permission ->
            scope.launch {
                SnackbarManager.showSnackbar(
                    SnackbarData(
                        message = when (permission) {
                            Manifest.permission.CAMERA ->
                                "Izin kamera diperlukan untuk mengambil foto"

                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                ->
                                "Izin galeri diperlukan"

                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                -> "Izin lokasi diperlukan"

                            Manifest.permission.POST_NOTIFICATIONS ->
                                "Izin notifikasi diperlukan"

                            else -> "Permission ditolak"
                        }
                    )
                )
            }
        }
    )

    // ===== UI =====
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7EFEA))
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            LoginHeader()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite)
            ) {
                LoginForm(
                    imei = imei,
                    employeeId = employeeId,
                    onEmployeeChange = { employeeId = it },
                    focusManager = focusManager,
                    onCopyImei = {
                        clipboardManager.setText(AnnotatedString(imei))
                        scope.launch {
                            SnackbarManager.showSnackbar(
                                SnackbarData("Data berhasil disalin")
                            )
                        }
                    },
                    onLoginConnection = onSettingConnection,
                    onLoginClick = {
                        if (employeeId.isBlank()) {
                            scope.launch {
                                SnackbarManager.showSnackbar(
                                    SnackbarData("Employee ID tidak boleh kosong")
                                )
                            }
                        } else {
                            viewModel.login(employeeId)
                        }
                    }
                )
            }
        }

        LoginFooter(versionApps)

        if (loginState?.status == StatusNetwork.LOADING) {
            CustomLoadingDialog()
        }
    }

    HandleLoginResult(loginState, onSuccessLoginSuccess)
}
