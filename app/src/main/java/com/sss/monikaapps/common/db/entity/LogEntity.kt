package com.sss.monikaapps.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDateTime
import com.sss.monikaapps.common.helper.GenerateRandomTextHelper.generateRandomId


@Entity(tableName = "log_table")
data class LogEntity(
    @PrimaryKey val id: String = generateRandomId(),
    val title: String,
    val description: String,
    val typeFeature: String,
    val createdAt: String = getCurrentDateTime(),
)