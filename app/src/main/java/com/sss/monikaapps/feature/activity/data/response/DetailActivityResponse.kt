package com.sss.monikaapps.feature.activity.data.response

import com.google.gson.annotations.SerializedName

data class DetailActivityResponse(

    @field:SerializedName("data")
    val data: DataItemDetailActivity? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,
)

data class DataItemDetailActivity(

    @field:SerializedName("foto_aktivitas")
    val fotoActivity: List<PhotoItem>? = null,

    @field:SerializedName("header")
    val header: DataHeaderDetailActivity? = null,
)

data class PhotoItem(

    @field:SerializedName("path")
    val path: String? = null,

    @field:SerializedName("folder_name")
    val folderName: String? = null,

    @field:SerializedName("id_photo")
    val id: String? = null,

    @field:SerializedName("tipe")
    val tipe: String? = null,
)

data class DataHeaderDetailActivity(

    @field:SerializedName("end_at")
    val endAt: String? = null,

    @field:SerializedName("end_lat")
    val endLat: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("trno")
    val trno: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("start_at")
    val startAt: String? = null,

    @field:SerializedName("is_sync")
    val isSync: String? = null,

    @field:SerializedName("start_lng")
    val startLng: String? = null,

    @field:SerializedName("id_karyawan")
    val employeeId: String? = null,

    @field:SerializedName("trno_mobile")
    val trnoMobile: String? = null,

    @field:SerializedName("start_lat")
    val startLat: String? = null,

    @field:SerializedName("end_lng")
    val endLng: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    var isSyncDataLocal: Int = 0,
)