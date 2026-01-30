package com.sss.monikaapps.feature.expense.data.response

import com.google.gson.annotations.SerializedName

data class MasterExpanseResponse(

    @field:SerializedName("data")
    val data: List<DataItemMasterExpense>? = null,

    @field:SerializedName("jumlah_data")
    val totalData: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,
)

data class DataItemMasterExpense(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("created_by")
    val createdBy: String? = null,

    @field:SerializedName("is_required_foto")
    val isRequiredFoto: String? = null,

    @field:SerializedName("last_modified_at")
    val lastModifiedAt: String? = null,
)
