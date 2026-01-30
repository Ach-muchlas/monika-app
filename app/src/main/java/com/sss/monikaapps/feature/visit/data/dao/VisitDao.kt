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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogActivity(data: LogEntity)

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