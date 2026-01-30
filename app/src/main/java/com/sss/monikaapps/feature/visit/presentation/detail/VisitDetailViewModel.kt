package com.sss.monikaapps.feature.visit.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.result.Result
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity
import com.sss.monikaapps.feature.visit.domain.usecase.FetchVisitDetailUseCase
import com.sss.monikaapps.feature.visit.domain.usecase.FetchVisitLocalDatabaseUseCase
import kotlinx.coroutines.launch

class VisitDetailViewModel(
    private val fetchVisitDetailUseCase: FetchVisitDetailUseCase,
) :
    ViewModel() {

    private val _visitResult = MutableLiveData<Result<List<VisitEntity>>>()
    val visitResult: LiveData<Result<List<VisitEntity>>> = _visitResult

    private val _visitDetailResult = MutableLiveData<Result<VisitEntity>>()
    val visitDetailResult: LiveData<Result<VisitEntity>> = _visitDetailResult


    fun fetchVisitDetail(idVisit : String) = viewModelScope.launch {
        _visitDetailResult.value = Result.loading(null)
        _visitDetailResult.value = fetchVisitDetailUseCase(idVisit)
    }

}