package com.sss.monikaapps.feature.expense.data.response

import com.google.gson.annotations.SerializedName

data class ExpansesResponse(

	@field:SerializedName("data")
	val data: List<DataItemExpenses>? = null,

	@field:SerializedName("jumlah_data")
	val jumlahData: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null,
)

data class DataItemExpenses(
	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("netamt")
	val netAmount: String? = null,

	@field:SerializedName("is_status")
	val isStatus: String? = null,

	@field:SerializedName("trno")
	val trno: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tanggal")
	val date: String? = null,
)
