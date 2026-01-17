package com.sss.monikaapps.feature.home.presentasi

import androidx.lifecycle.ViewModel
import com.sss.monikaapps.common.manager.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val sessionManager = SessionManager.getInstance()

    private val _user = MutableStateFlow(sessionManager.getDataUser())
    val user = _user.asStateFlow()

    init {
        _user.value = sessionManager.getDataUser()
    }
}