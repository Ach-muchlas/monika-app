package com.sss.monikaapps.feature.invoice.presentation.payment

import com.sss.monikaapps.feature.invoice.data.entity.BankReceiptEntity
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity

data class PaymentInvoiceUiState(
    val customerData: String = "",
    val nomorNota: String = "",
    val totalOutstanding: String = "",
    val totalNominalNota: String = "",
    val totalPaid: String = "",
    val statusPaid: Int = 0,
    val paymentMethodPaid: Int = 0,
    val paymentMethodReceipt: Int = 0,
    val reasons: List<ReasonEntity> = emptyList(),
    val selectedReason: ReasonEntity? = null,
    val listBankReceipt: List<BankReceiptEntity> = emptyList(),
    val selectedBankReceipt: BankReceiptEntity? = null,
    val selectedBankTransferPaid: BankReceiptEntity? = null,
    val selectedBankTransferBG: BankReceiptEntity? = null,
    val photos: List<String> = emptyList(),
    val dateTransfer: String? = null,
    val dateReceipt: String? = null,
    val dateCheck: String? = null,


    // for error state
    val statusError: String? = null,
    val methodPaymentError: String? = null,
    val methodPaymentReceiptError: String? = null,
    val dateTransferError: String? = null,
    val totalPaidError: String? = null,
    val reasonError: String? = null,
    val photoError: String? = null,
    val bankError: String? = null,
    val bankReceiptError: String? = null,
    val dateError: String? = null,
    val selectedBankBgError: String? = null,
    val dateBgError: String? = null,
    val totalPaidBgError: String? = null,
)