package com.sss.monikaapps.feature.invoice.data.remote

import android.util.Log
import com.sss.monikaapps.common.db.entity.LogEntity
import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.feature.invoice.data.response.CustomerInvoiceResponse
import com.sss.monikaapps.feature.invoice.data.response.NotaInvoiceResponse
import com.sss.monikaapps.network.ApiService

class InvoiceRemoteDataSourceImpl(private val apiService: ApiService) : InvoiceRemoteDataSource {
    override suspend fun getCustomerInvoice(): CustomerInvoiceResponse? {
        val response = apiService.fetchCustomerInvoice()

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun checkCustomerInvoice(totalData: Int): String? {
        return try {
            val response = apiService.checkCustomerInvoice(totalData)

            if (!response.isSuccessful) {
                null
            } else {
                response.body()?.data
            }
        } catch (e: Exception) {
            null
        }

    }

    override suspend fun getNotaInvoice(): NotaInvoiceResponse? {
        val response = apiService.fetchNotaInvoice()

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun checkNotaInvoice(totalData: Int): String? {
        return try {
            val response = apiService.checkNotaInvoice(totalData)

            if (!response.isSuccessful) {
                null
            } else {
                response.body()?.data
            }
        } catch (e: Exception) {
            null
        }
    }
}
