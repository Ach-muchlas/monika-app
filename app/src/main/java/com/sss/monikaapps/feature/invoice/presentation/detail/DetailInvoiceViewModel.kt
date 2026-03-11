package com.sss.monikaapps.feature.invoice.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.constanta.ArgumentsConstant.CUSTOMER_ID
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.invoice.domain.model.DetailInvoiceData
import com.sss.monikaapps.feature.invoice.domain.usecase.GetDetailInvoiceUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class DetailInvoiceViewModel(
    savedStateHandle: SavedStateHandle,
    detailInvoiceUseCase: GetDetailInvoiceUseCase,
) : ViewModel() {
     val customerId: String = checkNotNull(savedStateHandle[CUSTOMER_ID])
    val invoiceDetail: StateFlow<Result<DetailInvoiceData>> =
        detailInvoiceUseCase(customerId).map { customerDetail ->
            Result.success(customerDetail)
        }.onStart {
            emit(Result.loading(null))
        }.catch { exception ->
            emit(Result.error(null, exception.message.orEmpty()))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.loading(null)
        )


}
