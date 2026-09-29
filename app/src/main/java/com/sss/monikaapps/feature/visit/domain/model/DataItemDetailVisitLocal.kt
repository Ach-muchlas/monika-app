package com.sss.monikaapps.feature.visit.domain.model

import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.feature.visit.data.entity.VisitEntity

data class DataItemDetailVisitLocal(
    val header: VisitEntity,
    val photos: List<PhotoEntity>,
)
