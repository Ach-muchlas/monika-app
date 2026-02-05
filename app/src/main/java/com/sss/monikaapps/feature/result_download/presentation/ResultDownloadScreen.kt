package com.sss.monikaapps.feature.result_download.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.feature.download.presentation.DownloadViewModel
import com.sss.monikaapps.feature.download.presentation.component.CustomTableDownload
import com.sss.monikaapps.feature.result_download.presentation.component.BackupBottomSheet
import org.koin.androidx.compose.koinViewModel


@Composable
fun ResultDownloadScreen(
    navController: NavController,
    viewModel: DownloadViewModel = koinViewModel(),
    backUpViewModel: ResultDownloadViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    val configResult by viewModel.configDownloadResult
        .observeAsState(Result.loading(emptyList()))

    val backupResult by backUpViewModel.sendEmailResult
        .observeAsState()

    var showBackupSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchConfigDownload()
    }

    backupResult?.let {
        when (backupResult?.status) {
            StatusNetwork.LOADING -> {
                CustomLoadingDialog("Loading mengirim data ke server")
            }

            StatusNetwork.SUCCESS -> {
                LaunchedEffect(backupResult) {
                    SnackbarManager.showSnackbar(
                        SnackbarData(backupResult?.data.toString(), SnackbarType.SUCCESS)
                    )
                }
                showBackupSheet = false
            }

            StatusNetwork.ERROR -> {
                LaunchedEffect(backupResult) {
                    SnackbarManager.showSnackbar(
                        SnackbarData(backupResult?.message.toString(), SnackbarType.SUCCESS)
                    )
                }
                showBackupSheet = false
            }

            else -> Unit
        }
    }


    if (showBackupSheet) {
        BackupBottomSheet(
            onDismiss = { showBackupSheet = false },
            onSubmit = { reason ->
                backUpViewModel.exportAndSendDatabase(
                    context = context,
                    reason = reason
                )
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CustomTopBar(
            title = "Hasil Download",
            onBackClick = { navController.popBackStack() }
        )
        Spacer(modifier = Modifier.height(Dimens.LargeMargin))

        when (configResult.status) {
            StatusNetwork.LOADING -> {
                Text("Memuat data...")
            }

            StatusNetwork.SUCCESS -> {
                val data = configResult.data ?: emptyList()
                CustomTableDownload(data = data)
            }


            StatusNetwork.ERROR -> {
                Text("Gagal memuat tabel")
            }
        }

        Spacer(modifier = Modifier.height(Dimens.ExtraLargeMargin))

        CustomPrimaryButton(
            text = "Backup Data",
            onClick = {
                showBackupSheet = true
            }
        )
    }
}