package com.sss.monikaapps.common.mapper

import com.sss.monikaapps.R
import com.sss.monikaapps.common.model.Status
import com.sss.monikaapps.common.model.dataStatusExpanses
import com.sss.monikaapps.common.theme.PieGray

object MapperExpanse {

    fun mapperStatusExpanse(statusCode: String?): Status {
        return dataStatusExpanses.firstOrNull { it.id == statusCode } ?: Status(
            "?",
            "Unknown",
            R.drawable.icon_notification,
            PieGray
        )
    }
}