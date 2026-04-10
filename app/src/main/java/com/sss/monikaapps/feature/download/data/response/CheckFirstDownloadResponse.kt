package com.sss.monikaapps.feature.download.data.response

import com.google.gson.annotations.SerializedName

data class CheckFirstDownloadResponse(

	@field:SerializedName("data")
	val data: DataCheckFirstDownload? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataCheckFirstDownload(

	@field:SerializedName("isFirstDownload")
	val isFirstDownload: Boolean? = null
)
