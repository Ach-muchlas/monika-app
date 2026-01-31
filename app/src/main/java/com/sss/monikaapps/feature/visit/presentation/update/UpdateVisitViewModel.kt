package com.sss.monikaapps.feature.visit.presentation.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDateTime
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.visit.domain.usecase.CheckInVisitUseCase
import com.sss.monikaapps.feature.visit.domain.usecase.CheckOutVisitUseCase
import kotlinx.coroutines.launch

class UpdateVisitViewModel(
    private val checkInVisitUseCase: CheckInVisitUseCase,
    private val checkOutVisitUseCase: CheckOutVisitUseCase,
) : ViewModel() {

    private val _checkInResult = MutableLiveData<Result<String>>()
    val checkInResult: LiveData<Result<String>> = _checkInResult

    private val _checkOutResult = MutableLiveData<Result<String>>()
    val checkOutResult: LiveData<Result<String>> = _checkOutResult

    fun checkInVisit(
        idVisit: String,
        desc : String,
        startLat: String,
        startLng: String,
    ) {
        viewModelScope.launch {
            _checkInResult.value = Result.loading(null)
            val result = checkInVisitUseCase(idVisit, desc,getCurrentDateTime(), startLat, startLng)
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

}