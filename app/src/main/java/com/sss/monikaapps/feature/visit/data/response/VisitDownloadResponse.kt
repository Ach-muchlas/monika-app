package com.sss.monikaapps.feature.visit.data.response

import com.google.gson.annotations.SerializedName

data class VisitDownloadResponse(

    @field:SerializedName("data")
    val data: List<DataItemVisit>? = null,

    @field:SerializedName("jumlah_data")
    val totalData: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,
)

data class DataItemVisit(

    @field:SerializedName("CustName")
    val custName: String? = null,

    @field:SerializedName("Kota")
    val kota: String? = null,

    @field:SerializedName("Address")
    val address: String? = null,

    @field:SerializedName("GpsLatitude")
    val gpsLatitude: String? = null,

    @field:SerializedName("CustID")
    val custID: String? = null,

    @field:SerializedName("Kecamatan")
    val kecamatan: String? = null,

    @field:SerializedName("GpsLongitude")
    val gpsLongitude: String? = null,

    @field:SerializedName("Provinsi")
    val provinsi: String? = null,

    @field:SerializedName("Desa")
    val desa: String? = null,
)
