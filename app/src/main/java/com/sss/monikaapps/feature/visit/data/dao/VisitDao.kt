package com.sss.monikaapps.feature.visit.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sss.monikaapps.common.db.entity.LogEntity
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity

@Dao
interface VisitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerVisit(data: List<VisitEntity>)

    @Query(
        """
        SELECT * FROM visit_table
        WHERE (:keyword IS NULL 
               OR customerId LIKE '%' || :keyword || '%' 
               OR customerName LIKE '%' || :keyword || '%')
          AND (:statusFilter IS NULL 
               OR (:statusFilter = 0 AND syncStatus = 0)
               OR (:statusFilter = 1 AND syncStatus IN (1,2))
               OR (:statusFilter = 2 AND syncStatus IN (3,4))
               OR (:statusFilter = 3 AND syncStatus IN (1,3))
               OR (:statusFilter = 7)
              )
        ORDER BY customerId ASC 
    """
    )
    suspend fun fetchVisitLocalDatabase(
        keyword: String? = null,
        statusFilter: Int? = null,
    ): List<VisitEntity>

    @Query("SELECT * FROM visit_table WHERE syncStatus in(1,3)")
    suspend fun fetchVisitNotSync(): List<VisitEntity>

    @Query("SELECT * FROM visit_table WHERE id = :idVisit")
    suspend fun fetchVisitDetail(idVisit: String): VisitEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogVisit(data: LogEntity)

    @Query("SELECT count(*) from visit_table WHERE syncStatus in (1,2)")
    suspend fun countStillCheckIn(): Int

    @Query(
        """
        UPDATE visit_table 
        SET description = :desc,startLatitude = :startLat,startLongitude= :starLng, startAt =:timeCheckIn ,syncStatus = 1 
        WHERE id = :idVisit
    """
    )
    suspend fun checkInVisit(
        idVisit: String,
        desc: String,
        timeCheckIn: String,
        startLat: String,
        starLng: String,
    )

    @Query(
        """
        UPDATE visit_table 
        SET endLatitude = :endLat,endLongitude= :endLng, endAt =:timeCheckOut ,syncStatus = 3 
        WHERE id = :idVisit
    """
    )
    suspend fun checkOutVisit(
        idVisit: String,
        timeCheckOut: String,
        endLat: String,
        endLng: String,
    )

    @Query("SELECT COUNT(*) FROM visit_table")
    suspend fun countDataVisit(): Int

    @Query("DELETE FROM visit_table")
    suspend fun deleteVisit()

    @Query("UPDATE visit_table SET syncStatus = 1 WHERE id = :idMobile")
    suspend fun markCheckInIsDoneInLocal(idMobile: String)

    @Query("UPDATE visit_table SET trno = :trno, syncStatus = 2 WHERE id = :idMobile")
    suspend fun markCheckInIsSyncInServer(idMobile: String, trno: String)

    @Query("UPDATE visit_table SET syncStatus = 3 WHERE id = :idMobile")
    suspend fun markCheckOutIsDoneInLocal(idMobile: String)

    @Query("UPDATE visit_table SET syncStatus = 4 WHERE id = :idMobile")
    suspend fun markCheckOutIsSyncInServer(idMobile: String)


}