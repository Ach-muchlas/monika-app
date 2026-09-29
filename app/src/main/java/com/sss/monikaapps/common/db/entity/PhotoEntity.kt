package com.sss.monikaapps.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDateTime
import com.sss.monikaapps.common.helper.GenerateRandomTextHelper.generateRandomId

@Entity(tableName = "photo_table")
data class PhotoEntity(
    @PrimaryKey val id: String = generateRandomId(),
    val parentId: String,
    val parentFeature: Int,
    val parentType: String,
    val filePath: String,
    val createdAt: String = getCurrentDateTime(),
)
