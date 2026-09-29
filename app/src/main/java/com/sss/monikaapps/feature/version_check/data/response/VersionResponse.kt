package com.sss.monikaapps.feature.version_check.data.response

import com.google.gson.annotations.SerializedName

data class VersionResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Data(

	@field:SerializedName("version_name")
	val versionName: String? = null,

	@field:SerializedName("version_code")
	val versionCode: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("last_modified")
	val lastModified: Any? = null
)
