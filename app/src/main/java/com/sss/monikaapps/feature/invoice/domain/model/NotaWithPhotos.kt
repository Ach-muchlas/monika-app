package com.sss.monikaapps.feature.invoice.domain.model

import com.sss.monikaapps.feature.activity.data.response.PhotoItem
import com.sss.monikaapps.feature.invoice.data.entity.InvoiceEntity

data class NotaWithPhotos(
    val nota: InvoiceEntity,
    val lisPhoto: List<PhotoItem>,
)
