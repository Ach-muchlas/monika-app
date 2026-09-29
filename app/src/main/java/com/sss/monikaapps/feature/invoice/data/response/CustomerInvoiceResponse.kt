package com.sss.monikaapps.feature.invoice.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

data class CustomerInvoiceResponse(

	@SerializedName("data")
	val data: List<DataItemCustomerInvoice>? = null,

	@SerializedName("jumlah_data")
	val totalData: Int? = null,

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("status")
	val status: Int? = null
)

data class DataItemCustomerInvoice(

	@SerializedName("gpslongitude")
	val gpsLongitude: String? = null,

	@SerializedName("custid")
	val customerId: String? = null,

	@SerializedName("gpslatitude")
	val gpsLatitude: String? = null,

	@SerializedName("phones")
	val phones: String? = null,

	@SerializedName("custname")
	val customerName: String? = null,

	@SerializedName("alamat")
	val address: String? = null
)
