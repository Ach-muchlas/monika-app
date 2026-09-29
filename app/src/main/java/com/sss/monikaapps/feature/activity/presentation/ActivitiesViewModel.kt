package com.sss.monikaapps.feature.activity.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDateTime
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.data.response.DataItemActivities
import com.sss.monikaapps.feature.activity.data.response.DataItemDetailActivity
import com.sss.monikaapps.feature.activity.domain.usecase.CreateActivityUseCase
import com.sss.monikaapps.feature.activity.domain.usecase.FetchActivitiesUseCase
import com.sss.monikaapps.feature.activity.domain.usecase.FetchDetailActivityUseCase
import kotlinx.coroutines.launch

class ActivitiesViewModel(
    private val createActivityUseCase: CreateActivityUseCase,
    private val fetchActivitiesUseCase: FetchActivitiesUseCase,
    private val fetchDetailActivityUseCase: FetchDetailActivityUseCase,
) : ViewModel() {

    private val _activitiesResult = MutableLiveData<Result<List<DataItemActivities>>>()
    val activitiesResult: LiveData<Result<List<DataItemActivities>>> = _activitiesResult

    private val _detailActivityResult = MutableLiveData<Result<DataItemDetailActivity>>()
    val detailActivityResult: LiveData<Result<DataItemDetailActivity>> = _detailActivityResult

    private val _createResult = MediatorLiveData<Result<String>?>()
    val createResult: LiveData<Result<String>?> = _createResult

    private val _updateResult = MediatorLiveData<Result<String>?>()
    val updateResult: LiveData<Result<String>?> = _updateResult

    private val _syncManualResult = MediatorLiveData<Result<String>?>()
    val syncManualResult: LiveData<Result<String>?> = _syncManualResult

    fun fetchActivities() = viewModelScope.launch {
        _activitiesResult.value = Result.loading(null)
        _activitiesResult.value = fetchActivitiesUseCase()
    }

    fun fetchDetailActivity(trno: String) =
        viewModelScope.launch {
            _detailActivityResult.value = Result.loading(null)
            val result =
                fetchDetailActivityUseCase.fetchLocal(trno)
            _detailActivityResult.value = result
        }


    fun checkIn(payload: ActivityEntity) {
        viewModelScope.launch {
            _createResult.value = Result.loading(null)
            val result = createActivityUseCase.checkIn(payload)
            _createResult.value = result
        }
    }

    fun checkOut(idMobile: String, lat: String, lng: String) {
        viewModelScope.launch {
            _updateResult.value = Result.loading(null)
            val result = createActivityUseCase.checkOut(
                idMobile = idMobile,
                latitude = lat,
                longitude = lng,
                timeEnd = getCurrentDateTime()
            )
            _updateResult.value = result
        }
    }

    fun syncManual() {
        viewModelScope.launch {
            _syncManualResult.value = Result.loading(null)
            val result = createActivityUseCase.syncManualDataActivity()
            _syncManualResult.value = result
        }
    }

    fun clearSyncState() {
        _syncManualResult.value = null
    }


    fun clearCreateState() {
        _createResult.value = null
    }

    fun clearUpdateState() {
        _updateResult.value = null
    }
}