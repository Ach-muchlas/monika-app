package com.sss.monikaapps.feature.login.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
	val data: DataItemUserLogin? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Int? = null
)

data class DataItemUserLogin(

	@field:SerializedName("id_jabatan")
	val idRole: String? = null,

	@field:SerializedName("jabatan_level")
	val roleLevel: String? = null,

	@field:SerializedName("nama_karyawan")
	val employeeName: String? = null,

	@field:SerializedName("id_karyawan")
	val employeeId: String? = null,

	@field:SerializedName("id_depo")
	val idDepo: String? = null,

	@field:SerializedName("nama_depo")
	val namaDepo: String? = null,

	@field:SerializedName("imei")
	val imei: String? = null,

	@field:SerializedName("id_atasan")
	val idSuperior: String? = null,

	@field:SerializedName("nama_jabatan")
	val roleName: String? = null,

	@field:SerializedName("nama_atasan")
	val superiorName: String? = null,

	@field:SerializedName("tanggal_bergabung")
	val dateJoin: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
