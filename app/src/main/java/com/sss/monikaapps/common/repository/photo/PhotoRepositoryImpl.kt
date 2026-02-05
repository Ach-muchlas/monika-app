package com.sss.monikaapps.common.repository.photo

import com.sss.monikaapps.common.db.dao.PhotoDao
import com.sss.monikaapps.common.db.entity.PhotoEntity
import java.io.File

class PhotoRepositoryImpl(private val dao: PhotoDao) : PhotoRepository {
    override fun observePhotos(parentId: String, parentType: String) =
        dao.observePhotos(parentId, parentType)

    override suspend fun addPhoto(
        parentId: String,
        parentFeature: Int,
        parentType: String,
        path: String,
    ) {
        dao.insertPhotoActivity(
            PhotoEntity(
                parentId = parentId,
                parentType = parentType,
                parentFeature = parentFeature,
                filePath = path,
            )
        )
    }

    override suspend fun deletePhoto(photo: PhotoEntity) {
        File(photo.filePath).delete()
        dao.deleteById(photo.id)
    }
}