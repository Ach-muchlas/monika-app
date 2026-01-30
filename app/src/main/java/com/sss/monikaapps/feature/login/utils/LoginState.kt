package com.sss.monikaapps.feature.login.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.login.data.response.LoginResponse
import com.sss.monikaapps.common.snackbar.SnackbarManager

@Composable
fun HandleLoginResult(loginState: Result<LoginResponse>?, navigateToHome: () -> Unit) {
    loginState ?: return

    LaunchedEffect(loginState.status) {
        when (loginState.status) {
            StatusNetwork.SUCCESS -> {
                SnackbarManager.showSnackbar(
                    SnackbarData("Login berhasil", SnackbarType.SUCCESS)
                )
                navigateToHome()
            }

            StatusNetwork.ERROR -> {
                SnackbarManager.showSnackbar(
                    SnackbarData(
                        loginState.message ?: "Login gagal",
                        SnackbarType.ERROR
                    )
                )
            }

            else -> Unit
        }
    }
}