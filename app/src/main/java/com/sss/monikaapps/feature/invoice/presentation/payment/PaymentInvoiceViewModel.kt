package com.sss.monikaapps.feature.invoice.presentation.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.constanta.ArgumentsConstant.CUSTOMER_ID
import com.sss.monikaapps.common.constanta.ArgumentsConstant.ID_INVOICE
import com.sss.monikaapps.common.constanta.ArgumentsConstant.NOMOR_NOTA
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.invoice.data.entity.ReasonEntity
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceData
import com.sss.monikaapps.feature.invoice.domain.usecase.GetPaymentDataInvoiceUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.GetReasonUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.SubmitPaymentInvoiceUseCase
import com.sss.monikaapps.feature.invoice.domain.usecase.SyncManualInvoiceUseCase
import com.sss.monikaapps.feature.visit.domain.usecase.SyncManualVisitUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PaymentInvoiceViewModel(
    savedStateHandle: SavedStateHandle,
    paymentData: GetPaymentDataInvoiceUseCase,
    reasonData: GetReasonUseCase,
    private val submitPaymentUseCase: SubmitPaymentInvoiceUseCase,
) : ViewModel() {

    val nomorNota: String = checkNotNull(savedStateHandle[NOMOR_NOTA])
    val idInvoice: String = checkNotNull(savedStateHandle[ID_INVOICE])
    val customerId: String = checkNotNull(savedStateHandle[CUSTOMER_ID])

    private val _statusPaid = MutableStateFlow(1)
    val statusPaid = _statusPaid.asStateFlow()
    private val _totalPaid = MutableStateFlow("")
    val totalPaid = _totalPaid.asStateFlow()

    private val _selectedReason = MutableStateFlow<ReasonEntity?>(null)
    val selectedReason = _selectedReason.asStateFlow()

    private val _submitState = MutableStateFlow<Result<String>?>(null)
    val submitState = _submitState.asStateFlow()


    val paymentData: StateFlow<Result<PaymentInvoiceData>> = paymentData(nomorNota).map { data ->
        Result.success(data)
    }.catch { exception ->
        emit(Result.error(null, exception.message.orEmpty()))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = Result.loading(null)
    )

    val reason: StateFlow<Result<List<ReasonEntity>>> = reasonData().map { data ->
        Result.success(data)
    }.onStart {
        emit(Result.loading(null))
    }.catch { exception ->
        emit(Result.error(null, exception.message.orEmpty()))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.loading(null)
    )

    fun onStatusPaidChange(status: Int) {
        _statusPaid.value = status
        // Reset field lain jika status berubah
        if (status == 1) _selectedReason.value = null
        if (status == 2) _totalPaid.value = ""
    }

    fun onPaidChange(value: String) {
        _totalPaid.value = value
    }

    fun onReasonChange(reason: ReasonEntity) {
        _selectedReason.value = reason
    }

    fun submit(gpsLat: String, gpsLng: String) {
        val status = _statusPaid.value
        val amount = _totalPaid.value.ifEmpty { "0" }.toLong()
        val reasonId = _selectedReason.value?.idReason?.toInt() ?: 0
        val reasonName = _selectedReason.value?.descReason ?: "-"

        viewModelScope.launch {
            _submitState.value = Result.loading(null)

            val result = submitPaymentUseCase(
                nota = nomorNota,
                customerId = customerId,
                moneyPaid = amount,
                status = status,
                reasonId = reasonId,
                descReason = reasonName,
                gpsLat = gpsLat,
                gpsLng = gpsLng
            )

            _submitState.value = result
        }
    }

    fun clearSubmitPayment() {
        _submitState.value = null
    }


}