package com.sss.monikaapps.feature.mastering.domain.repository

import com.sss.monikaapps.feature.mastering.data.response.MasteringExpenseResponse

interface MasteringRepository {
    suspend fun fetchMasteringExpense() : MasteringExpenseResponse?
}