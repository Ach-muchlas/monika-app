package com.sss.monikaapps.feature.invoice.data.response

import com.google.gson.annotations.SerializedName

data class GeneratePdfResponse(

	@field:SerializedName("data")
	val data: List<DataItemInvoice?>? = null,

	@field:SerializedName("jumlah_data")
	val totalData: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataItemInvoice(

    @field:SerializedName("customer_address")
	val customerAddress: String? = null,

    @field:SerializedName("amount")
	val amount: String? = null,

    @field:SerializedName("distance_diff")
	val distanceDiff: String? = null,

    @field:SerializedName("desc_reason")
	val descReason: String? = null,

    @field:SerializedName("due_date")
	val dueDate: String? = null,

    @field:SerializedName("user_lat")
	val userLat: String? = null,

    @field:SerializedName("customer_lng")
	val customerLng: String? = null,

    @field:SerializedName("date_mobile")
	val dateMobile: String? = null,

    @field:SerializedName("idreason")
	val idreason: String? = null,

    @field:SerializedName("customer_lat")
	val customerLat: String? = null,

    @field:SerializedName("user_lng")
	val userLng: String? = null,

    @field:SerializedName("foto")
	val foto: List<FotoItem>? = null,

    @field:SerializedName("date_nota")
	val dateNota: String? = null,

    @field:SerializedName("customer_name")
	val customerName: String? = null,

    @field:SerializedName("nomor_nota")
	val nomorNota: String? = null,

    @field:SerializedName("customer_id")
	val customerId: String? = null,

    @field:SerializedName("outstanding_nota")
	val outstandingNota: String? = null,

    @field:SerializedName("payment")
	val payment: String? = null
)

data class FotoItem(

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("line_no")
	val lineNo: String? = null,

	@field:SerializedName("folder_name")
	val folderName: String? = null
)
