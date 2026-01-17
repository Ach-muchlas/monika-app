package com.sss.monikaapps.feature.activity.data.response

import com.google.gson.annotations.SerializedName

data class ActivitiesResponse(

    @field:SerializedName("data")
    val data: List<DataItemActivities>? = null,

    @field:SerializedName("jumlah_data")
    val totalData: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,
)

data class DataItemActivities(

    @field:SerializedName("end_at")
    val endAt: String? = null,

    @field:SerializedName("id_karyawan")
    val employeeId: String? = null,

    @field:SerializedName("trno_mobile")
    val trnoMobile: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("trno")
    val trno: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("start_at")
    val startAt: String? = null,

    @field:SerializedName("is_sync")
    val isSync: String? = null,

    // 0 : di local || 1 : di server
    var locationData: Int = 1,
)
