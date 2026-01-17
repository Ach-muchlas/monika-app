package com.sss.monikaapps.common.response

import com.google.gson.annotations.SerializedName

data class DefaultAddResponse(

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
