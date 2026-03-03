package com.sss.monikaapps.feature.visit.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import com.sss.monikaapps.feature.visit.domain.usecase.FetchVisitLocalDatabaseUseCase
import kotlinx.coroutines.launch

class VisitViewModel(
    private val fetchVisitLocalDatabaseUseCase: FetchVisitLocalDatabaseUseCase,
) : ViewModel() {

    private val _visitResult = MutableLiveData<Result<List<VisitEntity>>>()
    val visitResult: LiveData<Result<List<VisitEntity>>> = _visitResult

    private var currentKeyword: String? = null
    private var currentStatus: Int? = null

    fun fetchVisit(keyword: String?, status: Int?) = viewModelScope.launch {
        _visitResult.value = Result.loading(null)
        _visitResult.value = fetchVisitLocalDatabaseUseCase(keyword, status)
    }
    fun setStatus(status: Int) {
        currentStatus = status
        fetchVisit(currentKeyword, currentStatus)
    }

    fun setKeyword(keyword: String) {
        currentKeyword = keyword
        fetchVisit(currentKeyword, currentStatus)
    }


}