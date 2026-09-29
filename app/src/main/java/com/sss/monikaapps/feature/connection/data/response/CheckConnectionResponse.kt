package com.sss.monikaapps.feature.connection.data.response

import com.google.gson.annotations.SerializedName

data class CheckConnectionResponse(

	@field:SerializedName("data")
	val data: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
