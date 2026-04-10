package com.sss.monikaapps.feature.invoice.data.remote

import com.sss.monikaapps.common.helper.ResponseHelper.parseErrorResponse
import com.sss.monikaapps.common.response.DefaultAddResponse
import com.sss.monikaapps.feature.invoice.data.response.CustomerInvoiceResponse
import com.sss.monikaapps.feature.invoice.data.response.GeneratePdfResponse
import com.sss.monikaapps.feature.invoice.data.response.NotaInvoiceResponse
import com.sss.monikaapps.feature.invoice.data.response.ReasonInvoiceResponse
import com.sss.monikaapps.feature.invoice.domain.model.PaymentInvoiceRequest
import com.sss.monikaapps.feature.invoice.domain.model.toCreatedAtParts
import com.sss.monikaapps.feature.invoice.domain.model.toMultipartBody
import com.sss.monikaapps.feature.invoice.domain.model.toMultipartImageParts
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

    override suspend fun getReasonInvoice(): ReasonInvoiceResponse? {
        val response = apiService.fetchReasonInvoice()

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }
        return response.body()
    }

    override suspend fun checkReasonInvoice(totalData: Int): String? {
        return try {
            val response = apiService.checkReasonInvoice(totalData)

            if (!response.isSuccessful) {
                null
            } else {
                response.body()?.data
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun submitInvoice(payload: PaymentInvoiceRequest): DefaultAddResponse? {
        val response = apiService.submitInvoice(
            payload.toMultipartBody(),
            payload.toMultipartImageParts(),
            payload.toCreatedAtParts()
        )

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }

    override suspend fun getDataInvoiceMakeGeneratePdf(date: String): GeneratePdfResponse? {
        val response = apiService.fetchDataInvoiceMakePdf(date)

        if (!response.isSuccessful) {
            throw RuntimeException(parseErrorResponse(response))
        }

        return response.body()
    }
}
