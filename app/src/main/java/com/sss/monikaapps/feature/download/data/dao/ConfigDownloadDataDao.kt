package com.sss.monikaapps.feature.download.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity

@Dao
interface ConfigDownloadDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConfigDownload(data: ConfigDownloadDataEntity)

    @Upsert
    suspend fun upsertConfigDownload(data: ConfigDownloadDataEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM config_download_data_table WHERE tableName = :tableName)")
    suspend fun isTableExist(tableName: String): Boolean

    @Query("SELECT * FROM config_download_data_table")
    suspend fun fetchDataConfig(): List<ConfigDownloadDataEntity>

    @Query("SELECT COUNT(*) from config_download_data_table where statusTotalDownload = 0")
    suspend fun countPendingDownload(): Int

    @Query("UPDATE config_download_data_table SET  statusTotalDownload = 0,totalDataServer = 0,totalDataMobile = 0")
    suspend fun returnDataConfigDownload()
}