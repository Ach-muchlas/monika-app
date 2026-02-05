package com.sss.monikaapps.feature.download.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDate

@Entity(tableName = "config_download_data_table")
data class ConfigDownloadDataEntity(
    @PrimaryKey val id: Int,
    val tableName: String,
    val totalDataMobile: Int,
    val totalDataServer: Int,
    val statusTotalDownload: Boolean,
    val createAd: String = getCurrentDate(),
)
