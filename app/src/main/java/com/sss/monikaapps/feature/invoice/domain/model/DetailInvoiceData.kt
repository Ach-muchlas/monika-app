package com.sss.monikaapps.feature.invoice.domain.model

import com.sss.monikaapps.feature.invoice.data.entity.CustomerInvoiceEntity

data class DetailInvoiceData(
    val customerInvoice: CustomerInvoiceEntity,
    val listNota: List<NotaWithPhotos>,
)
