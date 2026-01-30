package com.sss.monikaapps.feature.expense.domain.model

import com.google.gson.annotations.SerializedName

data class ExpenseHeaderRequest(
    @SerializedName("tanggal") val date: String,
    val note: String,
)
