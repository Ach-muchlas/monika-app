package com.sss.monikaapps.feature.download.data.remote

import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDate
import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.download.data.response.CheckFirstDownloadResponse
import com.sss.monikaapps.feature.invoice.data.response.BankReceiptResponse
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse
import com.sss.monikaapps.network.ApiService

class DownloadRemoteDataSourceImpl(private val apiService: ApiService) : DownloadRemoteDataSource {
    override suspend fun fetchDownloadVisit(): VisitDownloadResponse? {
        val response = apiService.fetchDownloadVisit(getCurrentDate())

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun checkDataDownloadVisit(totalData: Int): String? {
        return try {
            val response = apiService.checkDataDownloadVisit(totalData)

            if (!response.isSuccessful) {
                null
            } else {
                response.body()?.data
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun fetchBankReceipt(): BankReceiptResponse? {
        val response = apiService.fetchBankReceipt()

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun checkBankReceipt(totalMobile : Int): String? {
        return try {
            val response = apiService.checkDataBankReceipt(totalMobile)

            if (!response.isSuccessful) {
                null
            } else {
                response.body()?.data
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun checkDownloadFirst(dateDownload: String): CheckFirstDownloadResponse {
        val response = apiService.fetchCheckFirstDownload(dateDownload)

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body() ?: throw RuntimeException("Response body null")
    }

}