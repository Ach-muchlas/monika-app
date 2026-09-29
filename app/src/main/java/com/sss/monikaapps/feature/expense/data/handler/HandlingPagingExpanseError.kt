package com.sss.monikaapps.feature.expense.data.handler

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.sss.monikaapps.common.component.CustomNotFoundAnimation
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.snackbar.SnackbarManager


@Composable
fun HandlePagingExpanseError(loadState: CombinedLoadStates) {
    val error = when {
        loadState.refresh is LoadState.Error ->
            (loadState.refresh as LoadState.Error).error

        loadState.append is LoadState.Error ->
            (loadState.append as LoadState.Error).error

        else -> null
    }

    LaunchedEffect(error) {
        error?.let {
            SnackbarManager.showSnackbar(
                SnackbarData(
                    it.message ?: "Gagal mengambil aktivitas",
                    SnackbarType.ERROR
                )
            )
        }
    }
}
