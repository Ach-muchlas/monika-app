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
    val paymentMethod: Int = 0,
    val reasons: List<ReasonEntity> = emptyList(),
    val selectedReason: ReasonEntity? = null,
    val listBankReceipt: List<BankReceiptEntity> = emptyList(),
    val selectedBankReceipt: BankReceiptEntity? = null,
    val photos: List<String> = emptyList(),
)