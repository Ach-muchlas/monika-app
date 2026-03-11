package com.sss.monikaapps.feature.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sss.monikaapps.common.helper.GenerateRandomTextHelper.generateRandomId

@Entity(tableName = "reason_table")
data class ReasonEntity(
    @PrimaryKey val id: String = generateRandomId(),

    val idReason: String,
    val descReason: String,
    val status: String,

    )