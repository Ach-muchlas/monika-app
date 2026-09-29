package com.sss.monikaapps.feature.invoice.presentation.payment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.constanta.ArgumentsConstant.CUSTOMER_ID
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_INVOICE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.NOMOR_NOTA
import com.sss.monikaapps.common.formatter.FormatterCurrency.formatCurrency
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_BG_CHECK
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_PAID
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_RECEIPT
import com.sss.monikaapps.feature.invoice.data.const.PaymentStatusConst.STATUS_UNPAID
import com.sss.monikaapps.feature.invoice.data.validate.ValidationForm
import com.sss.monikaapps.feature.invoice.domain.usecase.FetchBankReceiptUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.GetPaymentDataInvoiceUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.GetReasonUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.SubmitPaymentInvoiceUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentInvoiceViewModel(
    savedStateHandle: SavedStateHandle,
    paymentData: GetPaymentDataInvoiceUseCase,
    reasonData: GetReasonUseCase,
    private val fetchBankReceiptUseCase: FetchBankReceiptUseCase,
    private val submitPaymentUseCase: SubmitPaymentInvoiceUseCase,
) : ViewModel() {

    val nomorNota: String = checkNotNull(savedStateHandle[NOMOR_NOTA])
    val idInvoice: String = checkNotNull(savedStateHandle[ID_INVOICE])
    val customerId: String = checkNotNull(savedStateHandle[CUSTOMER_ID])

    private val _paymentMethod = MutableStateFlow(1)
    val paymentMethod = _paymentMethod.asStateFlow()

    private val _submitState = MutableStateFlow<Result<String>?>(null)
    val submitState = _submitState.asStateFlow()

    private val _state = MutableStateFlow(PaymentInvoiceUiState())
    val state: StateFlow<PaymentInvoiceUiState> = _state

    private val _requestLocation = MutableSharedFlow<Unit>()
    val requestLocation = _requestLocation.asSharedFlow()

    init {
        observeData(paymentData, reasonData)
    }

    private fun observeData(
        paymentDataUseCase: GetPaymentDataInvoiceUseCase,
        reasonDataUseCase: GetReasonUseCase,
    ) {
        // payment data
        viewModelScope.launch {
            paymentDataUseCase(nomorNota).collect { data ->
                _state.update {
                    it.copy(
                        customerData = "${data.customerId} - ${data.customerName}",
                        nomorNota = nomorNota,
                        totalOutstanding = formatCurrency(data.outstandingNota.toLong()),
                        totalNominalNota = formatCurrency(data.nominalNota.toLong())
                    )
                }
            }
        }

        // reason
        viewModelScope.launch {
            reasonDataUseCase().collect { list ->
                _state.update {
                    it.copy(reasons = list)
                }
            }
        }

        // bank
        viewModelScope.launch {
            fetchBankReceiptUseCase().collect { list ->
                _state.update {
                    it.copy(listBankReceipt = list)
                }
            }
        }
    }

    fun onEvent(event: PaymentInvoiceEvent) {
        when (event) {

            is PaymentInvoiceEvent.OnPaidChange -> {
                _state.update { it.copy(totalPaid = event.value, totalPaidError = null, totalPaidBgError = null) }
            }

            is PaymentInvoiceEvent.OnStatusPaidChange -> {
                _state.update {
                    it.copy(
                        statusPaid = event.status,
                        selectedReason = if (event.status == STATUS_UNPAID) it.selectedReason else null,
                        totalPaid = if (event.status == STATUS_PAID) it.totalPaid else "",
                        photoError = null,
                        statusError = null
                    )
                }
            }

            is PaymentInvoiceEvent.OnPaymentMethodChange -> {
                _state.update { data ->
                    if (data.statusPaid == STATUS_PAID) {
                        data.copy(
                            paymentMethodPaid = event.method,
                            selectedBankTransferPaid = if (event.method == 2) null else data.selectedBankTransferPaid,
                            methodPaymentError = null
                        )
                    } else {
                        data.copy(
                            paymentMethodReceipt = event.method, methodPaymentReceiptError = null
                        )
                    }

                }
            }

            is PaymentInvoiceEvent.OnReasonChange -> {
                _state.update { it.copy(selectedReason = event.reason, reasonError = null) }
            }

            is PaymentInvoiceEvent.OnBankSelected -> {
                _state.update { data ->
                    when (data.statusPaid) {
                        STATUS_PAID -> data.copy(
                            selectedBankTransferPaid = event.bank, bankError = null
                        )

                        STATUS_RECEIPT -> data.copy(
                            selectedBankReceipt = event.bank, bankReceiptError = null
                        )

                        else -> data.copy(
                            selectedBankTransferBG = event.bank, selectedBankBgError = null
                        )
                    }

                }
            }

            is PaymentInvoiceEvent.OnDateChange -> {
                _state.update {
                    when (it.statusPaid) {
                        STATUS_PAID -> it.copy(dateTransfer = event.date, dateTransferError = null)
                        STATUS_RECEIPT -> it.copy(
                            dateReceipt = event.date, dateError = null
                        )

                        else -> it.copy(dateCheck = event.date, dateBgError = null)
                    }
                }
            }

            is PaymentInvoiceEvent.OnSubmit -> {
                _state.update { it.copy(photos = event.photos) }
                val current = _state.value

                val result = ValidationForm.validate(current)

                if (!result.isValid()) {
                    _state.update {
                        it.copy(
                            statusError = result.statusPaymentError,
                            methodPaymentError = result.methodPaymentError,
                            methodPaymentReceiptError = result.methodPaymentReceiptError,
                            dateTransferError = result.dateTransfer,
                            totalPaidError = result.totalPaidError,
                            reasonError = result.reasonError,
                            photoError = result.photoError,
                            bankError = result.bankError,
                            dateError = result.dateError,
                            bankReceiptError = result.bankReceiptError,
                            selectedBankBgError = result.bankBgError,
                            dateBgError = result.dateBgError,
                            totalPaidBgError = result.moneyPaidBgError
                        )
                    }
                    return
                }

                viewModelScope.launch {
                    _requestLocation.emit(Unit)
                }
            }

            is PaymentInvoiceEvent.OnLocationResult -> {
                submit(
                    gpsLat = event.lat,
                    gpsLng = event.lng,
                )
            }

            is PaymentInvoiceEvent.ClearPhotoError -> {
                _state.update { it.copy(photoError = null) }
            }

            else -> Unit
        }
    }

    fun submit(gpsLat: String, gpsLng: String) {
        val current = _state.value

        val amount = current.totalPaid.ifEmpty { "0" }.toLong()
        val bank = when(current.statusPaid){
            STATUS_PAID -> current.selectedBankTransferPaid
            STATUS_RECEIPT -> current.selectedBankReceipt
            else -> current.selectedBankTransferBG
        }

        val dateReceiptOrTransfer = when(current.statusPaid){
            STATUS_PAID -> current.dateTransfer
            STATUS_BG_CHECK -> current.dateCheck
            else -> current.dateReceipt
        }

        val paymentMethod = if (current.statusPaid == STATUS_PAID) current.paymentMethodPaid else current.paymentMethodReceipt

        viewModelScope.launch {
            _submitState.value = Result.loading(null)

            val result = submitPaymentUseCase(
                nota = nomorNota,
                customerId = customerId,
                moneyPaid = amount,
                status = current.statusPaid,
                reasonId = current.selectedReason?.idReason?.toInt() ?: 0,
                descReason = current.selectedReason?.descReason ?: "-",
                gpsLat = gpsLat,
                gpsLng = gpsLng,
                dateReceipt = dateReceiptOrTransfer ?: "-",
                paymentMethod = paymentMethod.toString(),
                idCoa = bank?.idCoa ?: "0"
            )

            _submitState.value = result
        }
    }
}

