package com.sss.monikaapps.feature.expense.presentation.detail.handler

import androidx.compose.runtime.Composable
import com.sss.monikaapps.common.component.CustomConfirmDialog
import com.sss.monikaapps.feature.expense.presentation.detail.ExpenseDetailDialogAction
import com.sss.monikaapps.feature.expense.presentation.detail.ExpenseDetailViewModel

@Composable
fun ExpanseDetailActionHandler(
    action: ExpenseDetailDialogAction?,
    viewModel: ExpenseDetailViewModel,
    onClearAction: () -> Unit,
    onEditAction: (trno: String, idExpense: String, netAmount: String, note: String, initKm: String, finalKm: String) -> Unit,
) {
    when (action) {

        is ExpenseDetailDialogAction.Submit -> {
            CustomConfirmDialog(
                show = true,
                title = "Selesaikan Pengeluaran",
                message = "Setelah disimpan, data pengeluaran ini tidak dapat diubah. Lanjutkan?",
                confirmText = "Iya",
                onConfirm = {
                    viewModel.submitExpense(action.trno)
                    onClearAction()
                },
                onDismiss = {
                    onClearAction()
                }
            )
        }

        is ExpenseDetailDialogAction.UnSubmit -> {
            CustomConfirmDialog(
                show = true,
                title = "Batal Selesaikan Pengeluaran",
                message = "Status pengeluaran akan dibatalkan dan data dapat diubah kembali. Apakah Anda yakin?",
                confirmText = "Iya",
                onConfirm = {
                    viewModel.unSubmitExpense(action.trno)
                    onClearAction()
                },
                onDismiss = {
                    onClearAction()
                }
            )
        }

        is ExpenseDetailDialogAction.DeleteHeader -> {
            CustomConfirmDialog(
                show = true,
                title = "Hapus Data Pengeluaran",
                message = "Apakah kamu yakin ingin menghapus data pengeluaran ini?",
                confirmText = "Hapus",
                onConfirm = {
                    viewModel.deleteExpenseHeader(action.trno)
                    onClearAction()
                },
                onDismiss = {
                    onClearAction()
                }
            )
        }

        is ExpenseDetailDialogAction.DeleteDetail -> {
            CustomConfirmDialog(
                show = true,
                title = "Hapus Data Detail",
                message = "Apakah kamu yakin ingin menghapus data detail ini?",
                confirmText = "Hapus",
                onConfirm = {
                    viewModel.deleteExpenseDetail(action.trno, action.idDetail)
                    onClearAction()
                },
                onDismiss = {
                    onClearAction()
                }
            )
        }

        is ExpenseDetailDialogAction.EditDetail -> {
            CustomConfirmDialog(
                show = true,
                title = "Edit Data Detail",
                message = "Apakah kamu yakin ingin mengedit data detail pengeluaran ini?",
                confirmText = "Edit",
                onConfirm = {
                    onEditAction(
                        action.trno,
                        action.idDetail,
                        action.netAmount,
                        action.note,
                        action.initKm,
                        action.finalKm
                    )
                    onClearAction()
                },
                onDismiss = {
                    onClearAction()
                }
            )
        }

        null -> Unit
    }
}
