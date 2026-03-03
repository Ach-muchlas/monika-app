package com.sss.monikaapps.common.mapper

import com.sss.monikaapps.common.constanta.FeatureActivityConstant.CHECK_IN
import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.common.model.Status
import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.data.response.DataHeaderDetailActivity
import com.sss.monikaapps.feature.activity.data.response.DataItemActivities
import com.sss.monikaapps.feature.activity.data.response.PhotoItem

object MapperActivity {
    fun mapperActivities(data: List<ActivityEntity>): List<DataItemActivities> {
        return data.map { activity ->
            DataItemActivities(
                trnoMobile = activity.id,
                trno = activity.id,
                title = activity.title,
                description = activity.description,
                isSync = activity.activityStatus.toString(),
                startAt = activity.startAt,
                endAt = "",
                employeeId = "",
            )
        }
    }

    fun mapperDetailHeaderActivities(
        data: ActivityEntity?,
        employeeId: String,
    ): DataHeaderDetailActivity {
        return DataHeaderDetailActivity(
            endAt = data?.endAt ?: "",
            endLat = data?.endLatitude ?: "",
            description = data?.description ?: "",
            trno = data?.id ?: "",
            title = data?.title ?: "",
            startAt = data?.startAt ?: "",
            isSyncDataLocal = data?.syncStatus ?: 0,
            isSync = data?.activityStatus.toString(),
            startLng = data?.startLongitude ?: "",
            employeeId = employeeId,
            trnoMobile = data?.trno ?: "",
            startLat = data?.startLatitude ?: "",
            endLng = data?.endLongitude ?: "",
            id = data?.id ?: "",
        )
    }

    fun mapperPhotoEntityToPhotoResponse(data: List<PhotoEntity>): List<PhotoItem> {
        return data.map { photo ->
            PhotoItem(
                id = photo.parentId,
                path = photo.filePath,
                tipe = photo.parentType
            )
        }
    }

    fun Status.resolveTimeActivity(header: DataHeaderDetailActivity?): String {
        return if (id == CHECK_IN)
            header?.startAt.orEmpty()
        else
            header?.endAt.orEmpty()
    }

    fun Status.resolveLatActivity(header: DataHeaderDetailActivity?): String {
        return if (id == CHECK_IN)
            header?.startLat.orEmpty()
        else
            header?.endLat?.toString().orEmpty()
    }

    fun Status.resolveLngActivity(header: DataHeaderDetailActivity?): String {
        return if (id == CHECK_IN)
            header?.startLng?.toString().orEmpty()
        else
            header?.endLng?.toString().orEmpty()
    }
}