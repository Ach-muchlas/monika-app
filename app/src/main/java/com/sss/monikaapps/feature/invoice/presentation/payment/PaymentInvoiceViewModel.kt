package com.sss.monikaapps.feature.invoice.presentation.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.constanta.ArgumentsConstant.CUSTOMER_ID
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_INVOICE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.NOMOR_NOTA
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.invoice.domain.usecase.FetchBankReceiptUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.GetPaymentDataInvoiceUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.GetReasonUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.SubmitPaymentInvoiceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

//    private val _statusPaid = MutableStateFlow(1)
//    val statusPaid = _statusPaid.asStateFlow()

    private val _paymentMethod = MutableStateFlow(1)
    val paymentMethod = _paymentMethod.asStateFlow()

//    private val _totalPaid = MutableStateFlow("")
//    val totalPaid = _totalPaid.asStateFlow()

//    private val _selectedReason = MutableStateFlow<ReasonEntity?>(null)
//    val selectedReason = _selectedReason.asStateFlow()
//
//    private val _selectedBankReceipt = MutableStateFlow<BankReceiptEntity?>(null)
//    val selectedBankReceipt = _selectedBankReceipt.asStateFlow()

    private val _submitState = MutableStateFlow<Result<String>?>(null)
    val submitState = _submitState.asStateFlow()

//    val paymentData: StateFlow<Result<PaymentInvoiceData>> = paymentData(nomorNota).map { data ->
//        Result.success(data)
//    }.catch { exception ->
//        emit(Result.error(null, exception.message.orEmpty()))
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.Lazily,
//        initialValue = Result.loading(null)
//    )

//    val reason: StateFlow<Result<List<ReasonEntity>>> = reasonData().map { data ->
//        Result.success(data)
//    }.onStart {
//        emit(Result.loading(null))
//    }.catch { exception ->
//        emit(Result.error(null, exception.message.orEmpty()))
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = Result.loading(null)
//    )

    private val _state = MutableStateFlow(PaymentInvoiceUiState())
    val state: StateFlow<PaymentInvoiceUiState> = _state

//    val bankReceipts: StateFlow<List<BankReceiptEntity>> = fetchBankReceiptUseCase().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = emptyList()
//    )

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
                        totalOutstanding = data.outstandingNota.toString(),
                        totalNominalNota = data.nominalNota.toString()
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

//    fun onStatusPaidChange(status: Int) {
//        _statusPaid.value = status
//        // Reset field lain jika status berubah
//        if (status == 1) _selectedReason.value = null
//        if (status == 2) _totalPaid.value = ""
//    }

//    fun onPaymentMethodChange(paymentMethod: Int) {
//        _paymentMethod.value = paymentMethod
//
//        if (paymentMethod == 2) _selectedBankReceipt.value = null
//    }

//    fun onPaidChange(value: String) {
//        _totalPaid.value = value
//    }

//    fun onReasonChange(reason: ReasonEntity) {
//        _selectedReason.value = reason
//    }

//    fun onBankChange(reason: BankReceiptEntity) {
//        _selectedBankReceipt.value = reason
//    }

    fun onEvent(event: PaymentInvoiceEvent) {
        when (event) {

            is PaymentInvoiceEvent.OnPaidChange -> {
                _state.update { it.copy(totalPaid = event.value) }
            }

            is PaymentInvoiceEvent.OnStatusPaidChange -> {
                _state.update {
                    it.copy(
                        statusPaid = event.status,
                        selectedReason = if (event.status == 1) null else it.selectedReason,
                        totalPaid = if (event.status == 2) "" else it.totalPaid
                    )
                }
            }

            is PaymentInvoiceEvent.OnPaymentMethodChange -> {
                _state.update {
                    it.copy(
                        paymentMethod = event.method,
                        selectedBankReceipt = if (event.method == 2) null else it.selectedBankReceipt
                    )
                }
            }

            is PaymentInvoiceEvent.OnReasonChange -> {
                _state.update { it.copy(selectedReason = event.reason) }
            }

            is PaymentInvoiceEvent.OnBankSelected -> {
                _state.update { it.copy(selectedBankReceipt = event.bank) }
            }

            else -> Unit
        }
    }

//    fun submit(gpsLat: String, gpsLng: String, dateReceipt: String) {
//        val status = _statusPaid.value
//        val amount = _totalPaid.value.ifEmpty { "0" }.toLong()
//        val reasonId = _selectedReason.value?.idReason?.toInt() ?: 0
//        val reasonName = _selectedReason.value?.descReason ?: "-"
//        val paymentMethod = _paymentMethod.value
//        val bankReceipt = _selectedBankReceipt.value
//
//        Log.e(
//            "CHECK_DATA_SUBMIT",
//            "status : $status || amount: $amount || reason id : $reasonId || reason name : $reasonName"
//        )
//        Log.e(
//            "CHECK_DATA_SUBMIT",
//            "data payment method : $paymentMethod || bank receipt id coa: ${bankReceipt?.idCoa} || bank receipt : ${bankReceipt?.bankName}"
//        )
//
//        viewModelScope.launch {
//            _submitState.value = Result.loading(null)
//            Log.e(
//                "CHECK_DATA",
//                "data payment method : $paymentMethod || bank receipt id coa: ${bankReceipt?.idCoa} || bank receipt : ${bankReceipt?.bankName}"
//            )
//            val result = submitPaymentUseCase(
//                nota = nomorNota,
//                customerId = customerId,
//                moneyPaid = amount,
//                status = status,
//                reasonId = reasonId,
//                descReason = reasonName,
//                gpsLat = gpsLat,
//                gpsLng = gpsLng,
//                dateReceipt = dateReceipt,
//                paymentMethod = (paymentMethod ?: "0").toString(),
//                idCoa = bankReceipt?.idCoa ?: "-"
//            )
//
//            _submitState.value = result
//        }
//    }

    fun submit(gpsLat: String, gpsLng: String, dateReceipt: String) {
        val current = _state.value

        val amount = current.totalPaid.ifEmpty { "0" }.toLong()

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
                dateReceipt = dateReceipt,
                paymentMethod = current.paymentMethod.toString(),
                idCoa = current.selectedBankReceipt?.idCoa ?: "-"
            )

            _submitState.value = result
        }
    }
}

