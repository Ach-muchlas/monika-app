package com.sss.monikaapps.feature.invoice.data.response

import com.google.gson.annotations.SerializedName

data class NotaInvoiceResponse(

	@field:SerializedName("data")
	val data: List<DataItemNotaInvoice>? = null,

	@field:SerializedName("jumlah_data")
	val totalData: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataItemNotaInvoice(

	@field:SerializedName("custid")
	val customerId: String? = null,

	@field:SerializedName("nomornota")
	val nomorNota: String? = null,

	@field:SerializedName("outstandingnota")
	val outstandingNota: String? = null,

	@field:SerializedName("tanggalnota")
	val dateNota: String? = null,

	@field:SerializedName("jatuhtempo")
	val dueDate: String? = null
)
