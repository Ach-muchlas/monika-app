package com.sss.monikaapps.feature.visit.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sss.monikaapps.common.helper.GenerateRandomTextHelper.generateRandomId

@Entity(tableName = "visit_table")
data class VisitEntity(
    @PrimaryKey val id: String = generateRandomId(),

    val customerId: String,
    val customerName: String,
    val address: String,
    val desa: String,
    val kecamatan: String,
    val kota: String,
    val provinsi: String,
    val customerLatitude: String,
    val customerLongitude: String,

    val startAt: String? = null,
    val endAt: String? = null,
    val startLatitude: String? = null,
    val startLongitude: String? = null,
    val endLatitude: String? = null,
    val endLongitude: String? = null,

    val description: String? = null,
    val trno: String? = null,
    // 0 = belum visit || 1 = checkin sudah disimpan lokal || 2 = checkin sudah tersimpan di server
    // 3 = checkout sudah disimpan lokal || 4 = checkout sudah tersimpan di server
    val syncStatus: Int = 0,
)
