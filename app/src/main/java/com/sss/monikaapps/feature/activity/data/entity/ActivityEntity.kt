package com.sss.monikaapps.feature.activity.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDate

@Entity(tableName = "activity_table")
data class ActivityEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val createdAt: String = getCurrentDate(),
    val startAt: String? = null,
    val endAt: String? = null,
    val startLatitude: String? = null,
    val startLongitude: String? = null,
    val endLatitude: String? = null,
    val endLongitude: String? = null,

    // 1 check in || 2 check out
    val activityStatus: Int = 1,
    val trno: String? = null,
    // checkin 0 = belum dibuat || 1 = sudah disimpan lokal || 2 = sudah tersimpan di server
    // checkout 3 = sudah disimpan lokal || 4 = sudah tersimpan di server
    val syncStatus: Int = 0,
)
