package com.sss.monikaapps.feature.expense.presentation.detail.handler

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.sss.monikaapps.common.data.SnackbarType
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.common.snackbar.SnackbarManager.showSnackbar
import com.sss.monikaapps.feature.expense.presentation.detail.ExpenseDetailViewModel
import com.sss.monikaapps.feature.expense.presentation.detail.HandleResultActionExpense

@Composable
fun ExpanseDetailResultHandler(
    submitResult :Result<DefaultAddResponse>?,
    unSubmitResult :Result<DefaultAddResponse>?,
    deleteHeaderResult: Result<DefaultAddResponse>?,
    deleteDetailResult: Result<DefaultAddResponse>?,
    trno: String,
    navController: NavController,
    viewModel: ExpenseDetailViewModel,
) {
    HandleResultActionExpense(
        result = submitResult,
        onSuccess = { message ->
            showSnackbar(
                SnackbarData(message, SnackbarType.SUCCESS)
            )
            viewModel.fetchDetailExpanse(trno)
        }
    )

    HandleResultActionExpense(
        result = unSubmitResult,
        onSuccess = { message ->
            showSnackbar(
                SnackbarData(message, SnackbarType.SUCCESS)
            )
            viewModel.fetchDetailExpanse(trno)
        }
    )

    HandleResultActionExpense(
        result = deleteHeaderResult,
        onSuccess = { message ->
            showSnackbar(
                SnackbarData(message, SnackbarType.SUCCESS)
            )
            navController.popBackStack()
        }
    )

    HandleResultActionExpense(
        result = deleteDetailResult,
        onSuccess = { message ->
            showSnackbar(
                SnackbarData(message, SnackbarType.SUCCESS)
            )
            viewModel.fetchDetailExpanse(trno)
        }
    )
}
