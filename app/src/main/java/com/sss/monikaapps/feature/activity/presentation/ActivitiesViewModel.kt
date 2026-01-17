package com.sss.monikaapps.feature.activity.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sss.monikaapps.common.data.StatusNetwork
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.data.repository.ActivitiesRepository
import com.sss.monikaapps.feature.activity.data.response.DataItemActivities
import com.sss.monikaapps.feature.activity.data.response.DataItemDetailActivity
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDateTime
import com.sss.monikaapps.common.result.Result

class ActivitiesViewModel(private val repository: ActivitiesRepository) : ViewModel() {
    private val sessionManager = SessionManager.getInstance()
    private val user = sessionManager.getDataUser()

    private val _activitiesResult = MutableLiveData<Result<List<DataItemActivities>>>()
    val activitiesResult: LiveData<Result<List<DataItemActivities>>> = _activitiesResult

    private val _detailActivityResult = MutableLiveData<Result<DataItemDetailActivity>>()
    val detailActivityResult: LiveData<Result<DataItemDetailActivity>> = _detailActivityResult

    private val _createResult = MediatorLiveData<Result<String>>()
    val createResult: LiveData<Result<String>> = _createResult

    private val _updateResult = MediatorLiveData<Result<String>>()
    val updateResult: LiveData<Result<String>> = _updateResult

    fun fetchActivities() {
        repository.fetchDataActivities()
            .observeForever {
                _activitiesResult.value = it
            }
    }

    fun fetchDetailActivity(trno: String, locationData: Int) {
        if (locationData == 0) {
            repository.fetchDetailActivityLocalDatabase(trno, user.employeeName ?: "-")
                .observeForever {
                    _detailActivityResult.value = it
                }
        } else {
            repository.fetchDetailActivity(trno)
                .observeForever {
                    _detailActivityResult.value = it
                }
        }
    }

    fun createActivity(payload: ActivityEntity) {
        val source = repository.createActivity(payload)
        _createResult.addSource(source) {
            _createResult.value = it
            if (it.status != StatusNetwork.LOADING) {
                _createResult.removeSource(source)
            }
        }
    }

    fun updateActivity(trno: String, idMobile: String, lat: String, lng: String) {
        val source = repository.checkOutActivity(
            trno = trno,
            idMobile = idMobile,
            endTime = getCurrentDateTime(),
            latitude = lat,
            longitude = lng,
        )
        _updateResult.addSource(source) {
            _updateResult.value = it
            if (it.status != StatusNetwork.LOADING) {
                _updateResult.removeSource(source)
            }
        }
    }
}