package com.sss.monikaapps.feature.invoice.data.validate

import com.sss.monikaapps.common.formatter.FormatterCurrency.cleanCurrency
import com.sss.monikaapps.feature.invoice.data.const.PaymentMethodConst.PAYMENT_CASH
import com.sss.monikaapps.feature.invoice.data.const.PaymentMethodConst.PAYMENT_TRANSFER
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_BG_CHECK
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_PAID
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_RECEIPT
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_UNPAID
import com.sss.monikaapps.feature.invoice.presentation.payment.PaymentInvoiceUiState

object ValidationForm {

    fun validate(state: PaymentInvoiceUiState): ValidationResult {

        val paidAmount = cleanCurrency(state.totalPaid)
        val outstandingAmount = cleanCurrency(state.totalOutstanding)

        var statusPaymentError: String? = null
        var methodPaymentError: String? = null
        var methodPaymentReceiptError: String? = null
        var dateTransferError: String? = null
        var totalPaidError: String? = null
        var reasonError: String? = null
        var photoError: String? = null
        var bankError: String? = null
        var bankReceiptError: String? = null
        var dateError: String? = null
        var bankBgError: String? = null
        var dateBgError: String? = null
        var moneyPaidBgError: String? = null

        when (state.statusPaid) {
            0 -> statusPaymentError = "Status pembayaran harus di pilih"

            STATUS_PAID -> {
                when (state.paymentMethodPaid) {
                    0 -> methodPaymentError = "Metode pembayaran harus di pilih"

                    PAYMENT_CASH -> {
                        if (state.totalPaid.isBlank()) totalPaidError = "Uang tidak boleh kosong"
                        if (paidAmount <= 0) totalPaidError = "Uang tidak boleh 0"
                        if (paidAmount > outstandingAmount) totalPaidError = "Melebihi tagihan"
                    }

                    PAYMENT_TRANSFER -> {
                        if (state.selectedBankTransferPaid == null) bankError = "Bank wajib dipilih"
                        if (state.dateTransfer.isNullOrBlank()) dateTransferError =
                            "Tanggal wajib diisi"
                        if (state.totalPaid.isBlank()) totalPaidError = "Uang tidak boleh kosong"
                        if (paidAmount <= 0) totalPaidError = "Uang tidak boleh 0"
                        if (paidAmount > outstandingAmount) totalPaidError =
                            "Uang yang dibayar melebihi tagihan"
                        if (state.photos.isEmpty()) photoError = "Foto transfer harus diisi"

                    }
                }
            }

            STATUS_UNPAID -> {
                if (state.selectedReason == null) {
                    reasonError = "Alasan wajib diisi"
                }
                if (state.photos.isEmpty()) {
                    photoError = "Foto wajib"
                }
            }

            STATUS_RECEIPT -> {
                if (state.paymentMethodReceipt == 0) {
                    methodPaymentReceiptError = "Metode pembayaran harus di pilih"
                }

                if (state.paymentMethodReceipt == PAYMENT_TRANSFER && state.selectedBankReceipt?.idCoa.isNullOrEmpty()) {
                    bankReceiptError = "Bank wajib dipilih"
                }

                if (state.dateReceipt.isNullOrEmpty()) {
                    dateError = "Tanggal wajib dipilih"
                }

                if (state.photos.isEmpty()) {
                    photoError = "Foto tanda terima wajib"
                }
            }

            STATUS_BG_CHECK -> {
                if (state.selectedBankTransferBG == null) {
                    bankBgError = "Bank wajib dipilih"
                }

                if (state.dateCheck.isNullOrEmpty()) {
                    dateBgError = "Tanggal wajib dipilih"
                }

                if (state.totalPaid.isBlank()) moneyPaidBgError = "Uang tidak boleh kosong"
                if (paidAmount <= 0) moneyPaidBgError = "Uang tidak boleh 0"
                if (paidAmount > outstandingAmount) moneyPaidBgError = "Melebihi tagihan"

                if (state.photos.isEmpty()) {
                    photoError = "Foto BG atau cek wajib diisi"
                }
            }
        }

        return ValidationResult(
            statusPaymentError,
            methodPaymentError,
            methodPaymentReceiptError,
            dateTransferError,
            totalPaidError,
            reasonError,
            photoError,
            bankError,
            bankReceiptError,
            dateError,
            bankBgError,
            dateBgError,
            moneyPaidBgError
        )
    }

    data class ValidationResult(
        val statusPaymentError: String? = null,
        val methodPaymentError: String? = null,
        val methodPaymentReceiptError: String? = null,
        val dateTransfer: String? = null,
        val totalPaidError: String? = null,
        val reasonError: String? = null,
        val photoError: String? = null,
        val bankError: String? = null,
        val bankReceiptError: String? = null,
        val dateError: String? = null,
        val bankBgError: String? = null,
        val dateBgError: String? = null,
        val moneyPaidBgError: String? = null,
    ) {
        fun isValid(): Boolean {
            return statusPaymentError == null &&
                    methodPaymentError == null &&
                    methodPaymentReceiptError == null &&
                    dateTransfer == null &&
                    totalPaidError == null &&
                    reasonError == null &&
                    photoError == null &&
                    bankError == null &&
                    bankReceiptError == null &&
                    dateError == null &&
                    bankBgError == null &&
                    dateBgError == null &&
                    moneyPaidBgError == null
        }
    }
}
