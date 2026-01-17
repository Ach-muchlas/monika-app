package com.sss.monikaapps.feature.activity.utils

import com.sss.monikaapps.feature.activity.data.entity.ActivityEntity
import com.sss.monikaapps.feature.activity.presentation.ActivitiesViewModel
import com.sss.monikaapps.common.formatter.FormatterDate.getCurrentDateTime

object ActivitySubmitHandler {

    fun submit(
        isCheckOut: Boolean,
        trno: String,
        idMobile: String,
        activityId: String,
        title: String,
        desc: String,
        lat: String,
        lng: String,
        viewModel: ActivitiesViewModel,
    ) {
        if (isCheckOut) {
            viewModel.updateActivity(
                trno = trno,
                idMobile = idMobile,
                lat = lat,
                lng = lng
            )
        } else {
            val payload = ActivityEntity(
                id = activityId,
                title = title,
                description = desc,
                startAt = getCurrentDateTime(),
                startLatitude = lat,
                startLongitude = lng,
            )

            viewModel.createActivity(payload)
        }
    }
}

