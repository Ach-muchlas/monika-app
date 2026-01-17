package com.sss.monikaapps.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sss.monikaapps.feature.activity.data.dao.ActivityDao
import com.sss.monikaapps.common.db.dao.PhotoDao
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.common.db.entity.LogEntity
import com.sss.monikaapps.common.db.entity.PhotoEntity

@Database(
    entities = [ActivityEntity::class, PhotoEntity::class, LogEntity::class],
    version = 4,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun photoDao(): PhotoDao
}