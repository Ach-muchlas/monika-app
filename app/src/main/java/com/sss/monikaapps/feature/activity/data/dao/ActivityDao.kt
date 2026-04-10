package com.sss.monikaapps.feature.activity.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sss.monikaapps.common.db.entity.LogEntity
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(data: ActivityEntity)

    @Query("SELECT * FROM activity_table ORDER BY startAt DESC")
    suspend fun fetchActivity(): List<ActivityEntity>

    @Query("SELECT * FROM activity_table WHERE syncStatus in (1,3) ORDER BY startAt DESC")
    suspend fun fetchDataActivityLocalDatabase(): List<ActivityEntity>

    @Query("SELECT * FROM activity_table where id = :trno")
    suspend fun fetchDataDetailActivityLocalDatabase(trno: String): ActivityEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogActivity(data: LogEntity)

    @Query("SELECT count(*) from activity_table WHERE activityStatus = 1")
    suspend fun countStillCheckIn(): Int

    @Query("SELECT count(*) from activity_table WHERE syncStatus <> 4")
    fun countDataCheckoutNotSync() : Flow<Int>

    @Query("SELECT syncStatus from activity_table WHERE trno = :trnoMobile")
    suspend fun getSyncData(trnoMobile: String): Int


    // checkout
    @Query(
        """
        UPDATE activity_table SET
        activityStatus = 2,
        endAt = :checkOutTime,
        endLatitude = :latitude,
        endLongitude = :longitude
        WHERE id = :idActivity
    """
    )
    suspend fun updateActivity(
        idActivity: String,
        checkOutTime: String,
        latitude: String,
        longitude: String,
    )

    @Query("UPDATE activity_table SET syncStatus = 1 WHERE id = :idActivity")
    suspend fun markCheckInIsDoneInLocal(idActivity: String)

    @Query("UPDATE activity_table SET trno = :trno, syncStatus = 2 WHERE id = :idActivity")
    suspend fun markCheckInIsSyncInServer(idActivity: String, trno: String)

    @Query("UPDATE activity_table SET syncStatus = 3 WHERE id = :idActivity")
    suspend fun markCheckOutIsDoneInLocal(idActivity: String)

    @Query("UPDATE activity_table SET syncStatus = 4 WHERE id = :idActivity")
    suspend fun markCheckOutIsSyncInServer(idActivity: String)
}