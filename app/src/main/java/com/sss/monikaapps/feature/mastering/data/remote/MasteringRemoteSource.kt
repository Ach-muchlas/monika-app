package com.sss.monikaapps.feature.mastering.data.remote

import com.sss.monikaapps.feature.mastering.data.response.MasteringExpenseResponse

interface MasteringRemoteSource {
    suspend fun fetchMasteringExpense() : MasteringExpenseResponse?
}