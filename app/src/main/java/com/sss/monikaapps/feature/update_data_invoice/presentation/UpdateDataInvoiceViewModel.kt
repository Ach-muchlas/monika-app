package com.sss.monikaapps.feature.update_data_invoice.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.constanta.TableNameConstant.CUSTOMER_INVOICE_TABLE
import com.sss.monikaapps.common.constanta.TableNameConstant.NOTA_INVOICE_TABLE
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.download.domain.usecase.FetchConfigDownloadUseCase
import com.sss.monikaapps.feature.update_data_invoice.data.model.UpdateDataInvoiceResponse
import com.sss.monikaapps.feature.update_data_invoice.domain.usecase.UpdateDataInvoiceUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class UpdateDataInvoiceViewModel(
    private val updateData: UpdateDataInvoiceUseCase,
    private val fetchConfigDownloadUseCase: FetchConfigDownloadUseCase,
) : ViewModel() {

    private val _configDownloadResult = MutableLiveData<Result<List<ConfigDownloadDataEntity>>>()
    val configDownloadResult: LiveData<Result<List<ConfigDownloadDataEntity>>> =
        _configDownloadResult

    private val _updateDataResult = MutableLiveData<Result<UpdateDataInvoiceResponse>>()
    val updateDataResult: LiveData<Result<UpdateDataInvoiceResponse>> = _updateDataResult

    private val _messageEvent = MutableSharedFlow<String>()
    val messageEvent = _messageEvent.asSharedFlow()

    fun fetchConfigUpdated() {
        viewModelScope.launch {
            _configDownloadResult.value = Result.loading(null)

            val result = fetchConfigDownloadUseCase()

            val filtered = result.data
                ?.filter { it.tableName in listOf(CUSTOMER_INVOICE_TABLE, NOTA_INVOICE_TABLE) }

            _configDownloadResult.value = Result.success(filtered)
        }
    }

    fun fetchUpdatedDataInvoice() {
        viewModelScope.launch {

            _updateDataResult.value =
                Result.loading(null, 0f, "Menyiapkan...")

            val result = updateData.execute { progress, message ->
                _updateDataResult.value = Result.loading(null, progress, message)
            }

            _updateDataResult.value = result
        }
    }
}