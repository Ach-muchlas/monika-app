package com.sss.monikaapps.feature.invoice.data.response

import com.google.gson.annotations.SerializedName

data class BankReceiptResponse(

	@field:SerializedName("data")
	val data: List<DataItemBankReceipt>? = null,

	@field:SerializedName("jumlah_data")
	val totalData: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataItemBankReceipt(

	@field:SerializedName("kode_bank")
	val kodeBank: String? = null,

	@field:SerializedName("id_coa")
	val idCoa: String? = null,

	@field:SerializedName("nama_bank")
	val namaBank: String? = null
)
