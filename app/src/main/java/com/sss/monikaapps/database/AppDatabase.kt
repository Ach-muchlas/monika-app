package com.sss.monikaapps.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sss.monikaapps.common.db.dao.PhotoDao
import com.sss.monikaapps.common.db.entity.LogEntity
import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.feature.activity.data.dao.ActivityDao
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.download.data.dao.ConfigDownloadDataDao
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity
import com.sss.monikaapps.feature.invoice.data.dao.InvoiceDao
import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity
import com.sss.monikaapps.feature.visit.data.dao.VisitDao
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity

@Database(
    entities = [ActivityEntity::class, PhotoEntity::class, VisitEntity::class,
                LogEntity::class, ConfigDownloadDataEntity::class, InvoiceEntity::class, CustomerInvoiceEntity::class],
    version = 14,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun visitDao(): VisitDao
    abstract fun configDownload(): ConfigDownloadDataDao
    abstract fun photoDao(): PhotoDao
    abstract fun invoiceDao(): InvoiceDao

    companion object {
        const val DB_NAME = "app_database"
    }
}