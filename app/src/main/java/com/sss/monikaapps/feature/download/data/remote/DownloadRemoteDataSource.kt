package com.sss.monikaapps.feature.download.data.remote

import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.download.data.response.CheckFirstDownloadResponse
import com.sss.monikaapps.feature.invoice.data.response.BankReceiptResponse
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse

interface DownloadRemoteDataSource {
    suspend fun fetchDownloadVisit(): VisitDownloadResponse?
    suspend fun checkDataDownloadVisit(totalData: Int): String?
    suspend fun fetchBankReceipt(): BankReceiptResponse?
    suspend fun checkBankReceipt(totalMobile : Int): String?

    suspend fun checkDownloadFirst(dateDownload: String): CheckFirstDownloadResponse?
}