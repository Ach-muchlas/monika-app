package com.sss.monikaapps.feature.expense.data.response

import com.google.gson.annotations.SerializedName
import com.sss.monikaapps.feature.activity.data.response.PhotoItem

data class DetailExpanseResponse(

    @field:SerializedName("data") val data: DataItemDetailExpense? = null,

    @field:SerializedName("message") val message: String? = null,

    @field:SerializedName("status") val status: Int? = null,
)

data class DataItemDetailExpense(

    @field:SerializedName("header") val header: DataHeaderExpense? = null,

    @field:SerializedName("detail") val detail: List<DetailItemExpense>? = null,
)

data class DataHeaderExpense(

    @field:SerializedName("void_at") val voidAt: String? = null,

    @field:SerializedName("note") val note: String? = null,

    @field:SerializedName("trno") val trno: String? = null,

    @field:SerializedName("created_at") val createdAt: String? = null,

    @field:SerializedName("created_by") val createdBy: String? = null,

    @field:SerializedName("last_modified_at") val lastModifiedAt: String? = null,

    @field:SerializedName("jumlah_detail") val totalDetail: String? = null,

    @field:SerializedName("void_reason") val voidReason: String? = null,

    @field:SerializedName("netamt") val netAmount: String? = null,

    @field:SerializedName("is_status") val isStatus: String? = null,

    @field:SerializedName("id_karyawan") val employeeId: String? = null,

    @field:SerializedName("id") val id: String? = null,

    @field:SerializedName("tanggal") val date: String? = null,

    @field:SerializedName("void_by") val voidBy: String? = null,

    @field:SerializedName("namakaryawan") val employeeName: String? = null,
)

data class DetailItemExpense(

    @field:SerializedName("id_pengeluaran") val idExpanse: String? = null,

    @field:SerializedName("km_akhir") val finalKilometer: String? = null,

    @field:SerializedName("netamt") val netAmountDetail: String? = null,

    @field:SerializedName("foto") val photo: List<PhotoItem>? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("photo_total") val photoTotal: String? = null,

    @field:SerializedName("created_at") val createdAt: String? = null,

    @field:SerializedName("id") val id: String? = null,

    @field:SerializedName("trno_transaksi") val trnoTransaction: String? = null,

    @field:SerializedName("km_awal") val initialKilometer: String? = null,

    @field:SerializedName("last_modified_at") val lastModifiedAt: String? = null,

    @field:SerializedName("note") val note: String? = null,
)
