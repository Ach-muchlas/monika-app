package com.sss.monikaapps.feature.visit.presentation.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDateTime
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.visit.domain.usecase.CheckInVisitUseCase
import com.sss.monikaapps.feature.visit.domain.usecase.CheckOutVisitUseCase
import com.sss.monikaapps.feature.visit.domain.usecase.SyncManualVisitUseCase
import kotlinx.coroutines.launch

class UpdateVisitViewModel(
    private val checkInVisitUseCase: CheckInVisitUseCase,
    private val checkOutVisitUseCase: CheckOutVisitUseCase,
    private val syncManualVisitUseCase: SyncManualVisitUseCase,
) : ViewModel() {

    private val _checkInResult = MutableLiveData<Result<String>?>()
    val checkInResult: LiveData<Result<String>?> = _checkInResult

    private val _checkOutResult = MutableLiveData<Result<String>?>()
    val checkOutResult: LiveData<Result<String>?> = _checkOutResult

    private val _syncManualResult = MediatorLiveData<Result<String>?>()
    val syncManualResult: LiveData<Result<String>?> = _syncManualResult

    fun checkInVisit(
        idVisit: String,
        desc: String,
        startLat: String,
        startLng: String,
    ) {
        viewModelScope.launch {
            _checkInResult.value = Result.loading(null)
            val result =
                checkInVisitUseCase(idVisit, desc, getCurrentDateTime(), startLat, startLng)
            _checkInResult.value = result
        }
    }

    fun checkOutVisit(
        idVisit: String,
        endLat: String,
        endLng: String,
    ) {
        viewModelScope.launch {
            _checkOutResult.value = Result.loading(null)
            val result = checkOutVisitUseCase(idVisit, getCurrentDateTime(), endLat, endLng)
            _checkOutResult.value = result
        }
    }

    fun syncManual() {
        viewModelScope.launch {
            _syncManualResult.value = Result.loading(null)
            val result = syncManualVisitUseCase()
            _syncManualResult.value = result
        }
    }

    fun clearSyncState() {
        _syncManualResult.value = null
    }

    fun clearCheckInState() {
        _checkInResult.value = null
    }

    fun clearCheckOutState() {
        _checkOutResult.value = null
    }


}