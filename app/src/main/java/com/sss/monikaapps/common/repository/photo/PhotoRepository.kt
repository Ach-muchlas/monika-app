package com.sss.monikaapps.common.repository.photo

import com.sss.monikaapps.common.db.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun observePhotos(parentId: String, parentType: String): Flow<List<PhotoEntity>>
    suspend fun addPhoto(parentId: String,parentFeature: Int, parentType: String, path: String)
    suspend fun deletePhoto(photo: PhotoEntity)
}