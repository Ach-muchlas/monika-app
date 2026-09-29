package com.sss.monikaapps.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.repository.location.LocationRepository
import com.sss.monikaapps.common.result.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    private val _locationState = MutableStateFlow<Result<Pair<Double, Double>>?>(null)
    val locationState = _locationState.asStateFlow()

    fun fetchLocation(timeout: Long = 55000) {
        _locationState.value = Result.loading(null)

        viewModelScope.launch {
            val result = repository.getUserLocation(timeout)
            _locationState.value = result
        }
    }

    fun clearState() {
        _locationState.value = null
    }
}