package com.sss.monikaapps.feature.activity.data.mapper

import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckInRequest
import java.io.File

object ActivityRequestMapper {

    fun toCheckInRequest(
        entity: ActivityEntity,
        photos: List<PhotoEntity>,
    ): ActivityCheckInRequest {

        return ActivityCheckInRequest(
            title = entity.title,
            description = entity.description,
            startAt = entity.startAt.toString(),
            startLatitude = entity.startLatitude.toString(),
            startLongitude = entity.startLongitude.toString(),
            trnoMobile = entity.id,
            photoActivity = photos.map { File(it.filePath) }
        )
    }
}
