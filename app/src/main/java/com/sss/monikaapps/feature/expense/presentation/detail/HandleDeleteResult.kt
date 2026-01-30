package com.sss.monikaapps.feature.expense.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.sss.monikaapps.common.component.CustomLoadingDialog
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.common.snackbar.SnackbarManager

@Composable
fun HandleDeleteResult(
    result: Result<DefaultAddResponse>?,
    onSuccess: suspend (String) -> Unit,
) {
    result?.let {
        when (it.status) {
            StatusNetwork.LOADING -> {
                CustomLoadingDialog("Loading delete data")
            }

            StatusNetwork.SUCCESS -> {
                LaunchedEffect(it) {
                    onSuccess(it.data?.message.orEmpty())
                }
            }

            StatusNetwork.ERROR -> {
                LaunchedEffect(it) {
                    SnackbarManager.showSnackbar(
                        SnackbarData(it.message ?: "Terjadi kesalahan", SnackbarType.ERROR)
                    )
                }
            }
        }
    }
}
