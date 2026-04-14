package com.sss.monikaapps.feature.invoice.data.validate

import com.sss.monikaapps.common.formatter.FormatterCurrency.cleanCurrency
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_PAID
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_UNPAID
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity

object ValidationForm {

    fun validateFormPayment(
        statusPaid: Int,
        totalPaid: String,
        totalOutstanding: String,
        selectedReason: ReasonEntity?,
        photos: List<String>,
        onTotalPaidError: (String) -> Unit,
        onReasonError: (String) -> Unit,
        onPhotoError: (String) -> Unit,
    ): Boolean {
        var hasError = false

        if (statusPaid == STATUS_PAID) {
            val paidAmount = cleanCurrency(totalPaid)
            val outstandingAmount = cleanCurrency(totalOutstanding)
            when {
                totalPaid.isBlank() -> {
                    onTotalPaidError("Uang yang dibayarkan tidak boleh kosong")
                    hasError = true
                }

                paidAmount <= 0 -> {
                    onTotalPaidError("Uang yang dibayarkan tidak boleh 0")
                    hasError = true
                }

                paidAmount > outstandingAmount -> {
                    onTotalPaidError("Uang yang dibayarkan tidak boleh lebih dari total tagihan")
                    hasError = true
                }
            }
        }

        if (statusPaid == STATUS_UNPAID) {
            if (selectedReason == null || selectedReason.descReason.isEmpty()) {
                onReasonError("Alasan tidak bayar tidak boleh kosong")
                hasError = true
            }
            if (photos.isEmpty()) {
                onPhotoError("Minimal 1 foto harus ditambahkan sebagai bukti")
                hasError = true
            }
        }

        return hasError
    }

}