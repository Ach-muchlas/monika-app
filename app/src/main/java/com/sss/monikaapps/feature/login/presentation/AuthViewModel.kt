package com.sss.monikaapps.feature.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sss.monikaapps.feature.login.data.repository.AuthRepository
import com.sss.monikaapps.feature.login.data.model.LoginRequest
import com.sss.monikaapps.feature.login.data.response.LoginResponse

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun userLogin(payload: LoginRequest) {
        repository.userLogin(payload).observeForever {
            _loginResult.value = it
        }
    }
}