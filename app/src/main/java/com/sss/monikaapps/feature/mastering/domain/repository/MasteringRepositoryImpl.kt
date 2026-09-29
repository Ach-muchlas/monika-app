package com.sss.monikaapps.feature.mastering.domain.repository

import com.sss.monikaapps.feature.mastering.data.remote.MasteringRemoteSource
import com.sss.monikaapps.feature.mastering.data.response.MasteringExpenseResponse

class MasteringRepositoryImpl(private val remote: MasteringRemoteSource) : MasteringRepository {
    override suspend fun fetchMasteringExpense(): MasteringExpenseResponse? =
        remote.fetchMasteringExpense()
}