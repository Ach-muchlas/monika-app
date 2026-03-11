package com.sss.monikaapps.feature.invoice.data.response

import com.google.gson.annotations.SerializedName

data class ReasonInvoiceResponse(

    @field:SerializedName("data")
    val data: List<DataItemReason>? = null,

    @field:SerializedName("jumlah_data")
    val totalData: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,
)

data class DataItemReason(

    @field:SerializedName("descreason")
    val descreason: String? = null,

    @field:SerializedName("status_")
    val status: String? = null,

    @field:SerializedName("idreason")
    val idreason: String? = null,
)
