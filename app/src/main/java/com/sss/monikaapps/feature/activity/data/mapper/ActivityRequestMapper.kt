package com.sss.monikaapps.feature.activity.data.mapper

import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckInRequest
import com.sss.monikaapps.feature.activity.domain.model.ActivityCheckOutRequest
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

    fun toCheckOut(
        idMobile: String,
        timeEnd: String,
        latitude: String,
        longitude: String,
        photos: List<PhotoEntity>,
    ): ActivityCheckOutRequest {
        val photo = photos.map { File(it.filePath) }
        return ActivityCheckOutRequest(
            endAt = timeEnd,
            endLatitude = latitude,
            endLongitude = longitude,
            trnoMobile = idMobile,
            photoActivity = photo,
        )
    }
}
