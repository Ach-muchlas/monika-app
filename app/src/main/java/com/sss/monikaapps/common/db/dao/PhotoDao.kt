package com.sss.monikaapps.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sss.monikaapps.common.db.entity.LogEntity
import com.sss.monikaapps.common.db.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo_table WHERE parentId = :parentId AND parentType = :parentType ORDER BY createdAt ASC")
    fun observePhotos(parentId: String, parentType: String): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photo_table WHERE parentId = :parentId AND parentType = :parentType ORDER BY createdAt ASC")
    suspend fun fetchPhotoByParentIdAndParentType(
        parentId: String,
        parentType: String,
    ): List<PhotoEntity>

    @Query("SELECT * FROM photo_table WHERE parentId = :parentId ORDER BY createdAt ASC")
    suspend fun fetchPhotosByParentId(parentId: String): List<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotoActivity(data: PhotoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogPhoto(data: LogEntity)

    @Query("DELETE FROM photo_table WHERE id = :id")
    suspend fun deleteById(id: String)

}