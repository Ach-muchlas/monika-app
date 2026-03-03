package com.sss.monikaapps.feature.download.data.model

import com.sss.monikaapps.feature.invoice.data.response.CustomerInvoiceResponse
import com.sss.monikaapps.feature.invoice.data.response.NotaInvoiceResponse
import com.sss.monikaapps.feature.visit.data.response.VisitDownloadResponse

data class DownloadDataResponse(
    val visit : VisitDownloadResponse?,
    val customerInvoice : CustomerInvoiceResponse?,
    val notaInvoice : NotaInvoiceResponse?
)
